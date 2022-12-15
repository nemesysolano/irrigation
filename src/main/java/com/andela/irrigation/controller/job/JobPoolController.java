package com.andela.irrigation.controller.job;
import com.andela.irrigation.AsyncUtils;
import com.andela.irrigation.controller.BaseController;
import com.andela.irrigation.controller.backoffice.PlotController;
import com.andela.irrigation.dto.job.CreateJobRequest;
import com.andela.irrigation.dto.job.CreateJobResponse;
import com.andela.irrigation.service.backoffice.PlotService;
import com.andela.irrigation.service.job.JobPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Collectors;

import com.andela.irrigation.dto.job.GetReadySensorsResponse;
/**
 * <p>REST Interface to <code>com.andela.irrigation.service.backoffice.PlotService</code> service.</p>
 */
@RestController
@RequestMapping("job")
@CrossOrigin(origins = "*", maxAge=3600, methods={
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.HEAD,
        RequestMethod.OPTIONS
})
@Slf4j
public class JobPoolController extends BaseController {
    PlotService plotService;
    JobPoolService jobPoolService;
    public JobPoolController(PlotService plotService, JobPoolService jobPoolService) {
        this.plotService = plotService;
        this.jobPoolService = jobPoolService;
    }

    @GetMapping("ready-sensors/{time}")
    @ResponseStatus(HttpStatus.OK)
    public GetReadySensorsResponse getReadySensors(
            @PathVariable("time") @DateTimeFormat(iso=DateTimeFormat.ISO.TIME) LocalTime time
    ) {

        return GetReadySensorsResponse.builder()
            .sensorIdList(
                AsyncUtils.fetchAsync(jobPoolService.getReadySensors(timeToDateTime(time))
            )
            .stream()
            .map(sensor -> sensor.sensorId)
            .collect(Collectors.toList()))
            .build();

    }

    @PostMapping("create")
    @ResponseStatus(HttpStatus.OK)
    public CreateJobResponse createJob(@RequestBody CreateJobRequest request) {
        return CreateJobResponse.builder()
            .jobIdList(
                AsyncUtils
                    .fetchAsync(jobPoolService.submit(request.sensorIdList))
                    .stream().map(status -> status.jobStatusId).collect(Collectors.toList()))
            .build();
    }
    @GetMapping("running-count")
    @ResponseStatus(HttpStatus.OK)
    public Long getRunningJobsCount() {
        return AsyncUtils.fetchAsync(jobPoolService.getRunningJobsCount());
    }
}
