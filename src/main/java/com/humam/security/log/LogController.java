package com.humam.security.log;

import com.humam.security.utils.GenericResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Page<LogResponse> getLogs(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return logService.getLogs(token, pageable);
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
