package ru.skillbox.userservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.userservice.exceptions.AccountAlreadyExistsException;
import ru.skillbox.userservice.exceptions.NoSuchAccountException;
import ru.skillbox.userservice.exceptions.NotAuthException;
import ru.skillbox.userservice.model.dto.AccountByFilterDto;
import ru.skillbox.userservice.model.dto.AccountDto;
import ru.skillbox.userservice.model.dto.AccountRecoveryRq;
import ru.skillbox.userservice.model.dto.AccountSearchDto;
import ru.skillbox.userservice.model.entity.User;
import ru.skillbox.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServices {

    private final UserRepository userRepository;

    public String recoveryUserAccount(AccountRecoveryRq recoveryRequest) {
        // logic with DB
        return recoveryRequest.getEmail();
    }

    public AccountDto getUserAccount(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        return AccountDto.of(user.orElseThrow(() -> new NoSuchAccountException("Can't find Account with email: " + userEmail)));
    }

    public AccountDto updateUserAccount(AccountDto accountDto) {
        // logic with DB
        return new AccountDto();
    }

    public String deleteUserAccount(String userEmail) {
        if (userEmail == null) {
            throw new NotAuthException("No auth user found!");
        }
        userRepository.deleteUserByEmail(userEmail);
        return userEmail;
    }

    public String blockAccount(boolean block, long id) {
        User currentUser = userRepository.findById(id).orElseThrow(() -> new NoSuchAccountException("Not found such user!"));
        currentUser.setBlocked(block);
        userRepository.save(currentUser);
        return block ? "blocked" : "unblocked";
    }

    public List<AccountDto> getAllAccounts(Pageable page) {
        // TODO: когда в репозитории добавим пагинацию - нужно изменить вызов и передавать page
        List<User> users = userRepository.findAll();
        return users.stream().map(AccountDto::of).toList();
    }

    public long createAccount(AccountDto accountDto) {
        if (userRepository.findByEmail(accountDto.getEmail()).isPresent()) {
            throw new AccountAlreadyExistsException("Account with such email already registered!");
        }
        User user = User.of(accountDto);
        return userRepository.save(user).getId();
    }

    public AccountDto searchAccountByFilter(AccountByFilterDto filterDto) {
        // logic with DB
        return new AccountDto();
    }

    public AccountDto getAccountById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return AccountDto.of(user.orElseThrow(() -> new NoSuchAccountException("Can't find Account with id: " + id)));
    }

    public List<AccountDto> searchAccount(AccountSearchDto accountSearchDto, Pageable pageable) {
        // logic with DB
        return List.of(new AccountDto());
    }

    public List<Long> getAllIds() {
        return userRepository.findAll().stream().map(User::getId).toList();
    }

    public List<AccountDto> getAccountIds(Long[] ids, Pageable page) {
        // TODO: когда в репозитории добавим пагинацию - нужно изменить вызов и передавать page
        List<User> users = userRepository.findAllByIdIn(List.of(ids));
        return users.stream().map(AccountDto::of).toList();
    }
}
