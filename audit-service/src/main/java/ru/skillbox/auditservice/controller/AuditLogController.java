package ru.skillbox.auditservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.auditservice.model.dto.AuditLogDto;
import ru.skillbox.auditservice.model.dto.AuditSearchFilterDto;
import ru.skillbox.auditservice.service.AuditService;
import ru.skillbox.commonlib.util.SortCreatorUtil;
import ru.skillbox.commonlib.util.admin.AdminAccessUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/audit")
@SecurityRequirement(name = "bearerAuth")
public class AuditLogController {
    private final AuditService auditService;
    @PostMapping("/search")
    public List<AuditLogDto> searchLogs(@RequestBody AuditSearchFilterDto auditSearchFilterDto,
                                        @RequestParam(value = "page",defaultValue = "0") int page,
                                        @RequestParam(value = "size",defaultValue = "100") int size,
                                        @RequestParam(value = "sort") List<String> sort,
                                        HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return auditService.findAllByFilter(auditSearchFilterDto, PageRequest.of(page,size,SortCreatorUtil.createSort(sort)));
    }
}
