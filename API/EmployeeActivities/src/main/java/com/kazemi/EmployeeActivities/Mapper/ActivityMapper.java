package com.kazemi.EmployeeActivities.Mapper;

import com.kazemi.EmployeeActivities.DTO.ActivityDTO;
import com.kazemi.EmployeeActivities.Model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author fh.kazemi
 **/
@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityDTO toDto(Activity activity);
    List<ActivityDTO> toDtoList(List<Activity> activityList);

    Activity toEntity(ActivityDTO activityDTO);
    List<Activity> toEntityList(List<ActivityDTO> activityDTOList);
}
