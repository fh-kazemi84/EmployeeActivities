package com.kazemi.EmployeeActivities.Service;

import com.kazemi.EmployeeActivities.Model.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author fh.kazemi
 **/
@Service
public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);
    private Workbook workbook;
    private Sheet sheet;
    private String filePath;

    public void load(String filePath, String sheetName) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("There is no file!");
        }

        this.filePath = filePath;
        FileInputStream fileInputStream = new FileInputStream(file);
        this.workbook = new XSSFWorkbook(fileInputStream);
        this.sheet = workbook.getSheet(sheetName);

        if (this.sheet == null) {
            throw new IllegalArgumentException("Sheet  " + sheetName + " not found");
        }
    }

    public CellInfo readCell(String cellAddress) {
        validateWorkbookAndSheet();

        CellReference cellReference = new CellReference(cellAddress);
        Cell cell = getCell(cellReference);
        if (cell == null) {
            return null;
        }

        return buildCellInfo(cellReference, cell);
    }

    private void validateWorkbookAndSheet() {
        if (workbook == null || sheet == null) {
            throw new IllegalStateException("Load Excel-File!");
        }
    }

    private Cell getCell(CellReference cellReference) {
        Row row = getRow(cellReference.getRow());
        if (row == null) return null;
        return row.getCell(cellReference.getCol());
    }

    public Row getRow(int rowIndex) {
        validateWorkbookAndSheet();
        return sheet.getRow(rowIndex);
    }

    private CellInfo buildCellInfo(CellReference cellReference, Cell cell) {
        CellInfo cellInfo = new CellInfo();
        cellInfo.setRow(cellReference.getRow());
        cellInfo.setColumn(cellReference.getCol());
        cellInfo.setContent(getCellContent(cell));

        if (cell.getCellComment() != null) {
            cellInfo.setComment(cell.getCellComment().getString().getString());
        }

        Style style = getCellStyle(cell);
        cellInfo.setStyle(style);

        return cellInfo;
    }

    public void close() throws Exception {
        if (workbook != null) {
            workbook.close();
        }
        workbook = null;
        sheet = null;
    }

    public String getCellContent(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private Style getCellStyle(Cell cell) {
        if (!(cell.getCellStyle() instanceof XSSFCellStyle)) {
            return null;
        }

        XSSFCellStyle xssfStyle = (XSSFCellStyle) cell.getCellStyle();
        Style style = new Style();

        // Pattern
        style.setPattern(getPattern(xssfStyle));

        // Gradient
        Gradient gradient = getGradientFromStyles(cell);
        style.setGradient(gradient);

        // Solid Color
        if (gradient != null) {
            style.setForegroundColor(null);
        } else {
            style.setForegroundColor(getSolidForegroundColor(xssfStyle));
        }

        // Borders
        Border border = getBorder(xssfStyle);
        style.setBorder(border);

        return style;
    }

    private String getPattern(XSSFCellStyle xssfStyle) {
        return xssfStyle.getFillPattern() != null ? xssfStyle.getFillPattern().name() : "";
    }

    private Integer getSolidForegroundColor(XSSFCellStyle xssfStyle) {
        if (xssfStyle.getFillForegroundColorColor() instanceof XSSFColor) {
            XSSFColor color = xssfStyle.getFillForegroundColorColor();
            byte[] rgb = color.getRGB();
            if (rgb != null && rgb.length == 3) {
                return rgbBytesToInt(rgb);
            }
        }
        return 0xFFFFFF; // default white
    }

    // Extract gradient from styles.xml
    private Gradient getGradientFromStyles(Cell cell) {
        try (ZipFile zipFile = new ZipFile(filePath)) {
            ZipEntry stylesEntry = zipFile.getEntry("xl/styles.xml");
            if (stylesEntry == null) return null;

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(zipFile.getInputStream(stylesEntry));
            doc.getDocumentElement().normalize();

            int fillId = (int) ((XSSFCellStyle) cell.getCellStyle()).getCoreXf().getFillId();
            NodeList fills = doc.getElementsByTagName("fill");
            if (fillId >= fills.getLength()) return null;

            Node fillNode = fills.item(fillId);
            NodeList children = fillNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeName().equals("gradientFill")) {
                    NodeList stops = ((Element) child).getElementsByTagName("stop");
                    if (stops.getLength() >= 2) {
                        Gradient g = new Gradient();
                        g.setGradientStartColor(parseColor(((Element) stops.item(0)).getElementsByTagName("color").item(0)));
                        g.setGradientStopColor(parseColor(((Element) stops.item(1)).getElementsByTagName("color").item(0)));
                        if (((Element) child).hasAttribute("degree")) {
                            g.setGradientDegree(Double.parseDouble(((Element) child).getAttribute("degree")));
                        } else {
                            g.setGradientDegree(0);
                        }
                        return g;
                    }
                }
            }

        } catch (Exception e) {
            logger.warn("Failed to read gradient from styles.xml", e);
        }
        return null;
    }

    private Integer parseColor(Node colorNode) {
        if (!(colorNode instanceof Element)) return null;
        Element e = (Element) colorNode;
        if (e.hasAttribute("rgb")) {
            String rgb = e.getAttribute("rgb");
            if (rgb.length() == 8 && rgb.startsWith("FF")) {
                rgb = rgb.substring(2);
            }
            return Integer.parseInt(rgb, 16);
        }
        return null;
    }

    private int rgbBytesToInt(byte[] rgbBytes) {
        if (rgbBytes == null || rgbBytes.length < 3) {
            return 0xFFFFFF;
        }
        return (rgbBytes[0] & 0xFF) << 16 | (rgbBytes[1] & 0xFF) << 8 | (rgbBytes[2] & 0xFF);
    }

    private Border getBorder(XSSFCellStyle xssfStyle) {
        Border border = new Border();

        String borderStyle = extractBorderStyle(xssfStyle);
        border.setBorderStyle(borderStyle);

        Integer borderColor = extractBorderColor(xssfStyle);
        border.setBorderColor(borderColor);

        return border;
    }

    private String extractBorderStyle(XSSFCellStyle xssfStyle) {
        BorderStyle[] borderStyles = {
                xssfStyle.getBorderTop(),
                xssfStyle.getBorderBottom(),
                xssfStyle.getBorderLeft(),
                xssfStyle.getBorderRight()
        };
        for (BorderStyle b : borderStyles) {
            if (b != BorderStyle.THICK) {
                return "THICK";
            }
        }
        return null;
    }

    private Integer extractBorderColor(XSSFCellStyle xssfStyle) {
        int targetColor = 16750848; // r=255, g=153, b=0
        XSSFColor[] colors = {
                xssfStyle.getTopBorderXSSFColor(),
                xssfStyle.getBottomBorderXSSFColor(),
                xssfStyle.getLeftBorderXSSFColor(),
                xssfStyle.getRightBorderXSSFColor()
        };
        for (XSSFColor c : colors) {
            if (c != null && c.getRGB() != null) {
                int intColor = rgbBytesToInt(c.getRGB());
                if (intColor == targetColor) {
                    return targetColor;
                }
            }
        }
        return null;
    }
}
