package com.humam.security.log;

import com.humam.security.utils.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class LogController {

    private final LogService logService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('logs:read')")
    public ResponseEntity<GenericResponse<List<LogResponse>>> getLogs(
            @RequestHeader("Authorization") String token
    ) {
        try {
            var logs = logService.getLogs(token);
            return ResponseEntity.ok(GenericResponse.success(logs, "logs fetched successfully"));
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              GenericResponse.error(exception.getMessage())
            );
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    GenericResponse.error(exception.getMessage())
            );
        }
    }

    @GetMapping("/{type}")
    @PreAuthorize("hasAuthority('logs:read')")
    public ResponseEntity<GenericResponse<List<LogResponse>>> getLogsByType(
            @RequestHeader("Authorization") String token,
            @PathVariable LogType type
    ) {
        try{
            var logs = logService.getByType(token, type);
            return ResponseEntity.ok(GenericResponse.success(logs, "logs fetched successfully"));
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(exception.getMessage())
            );
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    GenericResponse.error(exception.getMessage())
            );
        }
    }
}
