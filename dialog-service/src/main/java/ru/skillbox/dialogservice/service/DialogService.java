package ru.skillbox.dialogservice.service;


import org.springframework.stereotype.Service;
import ru.skillbox.dialogservice.model.dto.GetDialogsRs;
import ru.skillbox.dialogservice.model.dto.GetMessagesRs;
import ru.skillbox.dialogservice.model.dto.SetStatusMessageReadRs;
import ru.skillbox.dialogservice.model.dto.UnreadCountRs;
@Service
public class DialogService {
    public SetStatusMessageReadRs setStatusRead(Long companionId,
                                                Long currentAuthUserId) {
        return new SetStatusMessageReadRs();
    }

    public GetDialogsRs getDialogs(int offset,
                                   int itemPerPage,
                                   Long currentAuthUserId) {
        return new GetDialogsRs();
    }

    public UnreadCountRs getUnreaded(
            Long currentAuthUserId) {
        return new UnreadCountRs();
    }

    public GetMessagesRs getMessages(Long companionId,
                                     int offset,
                                     int itemPerPage,
                                     Long currentAuthUserId) {
        return new GetMessagesRs();
    }
}
