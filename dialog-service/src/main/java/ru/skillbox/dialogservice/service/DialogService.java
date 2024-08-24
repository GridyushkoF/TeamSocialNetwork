package ru.skillbox.dialogservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dialogservice.exception.NotFoundException;
import ru.skillbox.dialogservice.mapper.DialogMapper;
import ru.skillbox.dialogservice.model.dto.DialogDto;
import ru.skillbox.dialogservice.model.entity.Dialog;
import ru.skillbox.dialogservice.repository.DialogRepository;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DialogService {
    private final DialogRepository dialogRepository;
    private final MessageService messageService;
    private final DialogMapper dialogMapper;
    private static final String MEMBER_1 = "member1Id";
    private static final String MEMBER_2 = "member2Id";
    public Page<DialogDto> getDialogs(int page,
                                      String sort,
                                      Long currentAuthUserId) {

        String[] sorts = sort.split(",");
        Pageable nextPage = PageRequest.of(page, Integer.MAX_VALUE,
                Sort.by(Sort.Direction.fromString(sorts[1]), sorts[0]));

        List<Dialog> dialogs = dialogRepository.findAll(
                getAuthUserDialogsSpecification(currentAuthUserId), nextPage).stream().toList();

        log.info("Get dialogs {}", dialogs);

        List<DialogDto> pageList = dialogs.stream().map(dialogMapper::toDialogDto).toList();

        return new PageImpl<>(pageList, nextPage, dialogs.size());
    }

    private static Specification<Dialog> getDialogSpecification(Long authUserId, Long partnerId) {
        return (root, cq, cb) -> cb.or(
                cb.and(
                        cb.equal(root.get(MEMBER_1), authUserId),
                        cb.equal(root.get(MEMBER_2), partnerId)
                ),
                cb.and(
                        cb.equal(root.get(MEMBER_1), partnerId),
                        cb.equal(root.get(MEMBER_2), authUserId)
                )
        );
    }
    private static Specification<Dialog> getAuthUserDialogsSpecification(Long authUserId) {
        return (root, cq, cb) ->
                cb.or(
                        cb.equal(root.get(MEMBER_1), authUserId),
                        cb.equal(root.get(MEMBER_2), authUserId)
                );
    }

    private Dialog getDialogById(Long id) {
        return dialogRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                MessageFormat.format("Диалог с id {0} не найден", id)
                        )
                );
    }

    @Transactional
    public void incrementUnreadMessagesCount(Long id) {
        Dialog dialog = getDialogById(id);
        dialog.setUnreadCount(dialog.getUnreadCount() + 1);
        dialogRepository.save(dialog);
    }

    @Transactional
    public DialogDto getDialog(Long authUserId, Long partnerId) {
        DialogDto dialogDto = dialogMapper.toDialogDto(
                dialogRepository.findAll(getDialogSpecification(authUserId, partnerId))
                        .stream()
                        .findFirst()
                        .orElseGet(() -> createNewDialog(authUserId, partnerId))
        );
        log.info("Get dialog dialog id {}", dialogDto);
        return dialogDto;
    }

    @Transactional
    public DialogDto updateDialog(Long authUserId, Long id) {
        Dialog dialog = getDialogById(id);
        dialog.setUnreadCount(0);
        messageService.updateDialogMessages(authUserId, id);
        log.info("Update dialog id - {}", id);
        return dialogMapper.toDialogDto(
                dialogRepository.save(
                        dialog
                )
        );
    }

    public Dialog createNewDialog(Long authUserId, Long partnerId) {
        Dialog newDialog = new Dialog();
        newDialog.setMember1Id(authUserId);
        newDialog.setMember2Id(partnerId);
        newDialog.setUnreadCount(1);
        return dialogRepository.save(newDialog);
    }
}
