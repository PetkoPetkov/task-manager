package io.resolve.task.manager.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private String dueDate;

    private String status;

    private Long userId;

    private Long dependsOn;
}
