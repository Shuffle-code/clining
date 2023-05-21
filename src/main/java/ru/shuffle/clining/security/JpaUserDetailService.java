package ru.shuffle.clining.security;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shuffle.clining.dao.JanitorImageDao;
import ru.shuffle.clining.dao.security.AccountRoleDao;
import ru.shuffle.clining.dao.security.AccountUserDao;
import ru.shuffle.clining.dao.security.ConfirmationCodeDao;
import ru.shuffle.clining.dto.UserDto;
import ru.shuffle.clining.entity.Janitor;
import ru.shuffle.clining.entity.JanitorImage;
import ru.shuffle.clining.entity.enums.Status;
import ru.shuffle.clining.entity.security.AccountRole;
import ru.shuffle.clining.entity.security.AccountUser;
import ru.shuffle.clining.entity.security.ConfirmationCode;
import ru.shuffle.clining.entity.security.enums.AccountStatus;
import ru.shuffle.clining.exception.UsernameAlreadyExistsException;
import ru.shuffle.clining.service.JanitorService;
import ru.shuffle.clining.service.UserService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class JpaUserDetailService implements UserDetailsService, UserService {

    private final String imageName = "image104-66.jpg";
    private final AccountUserDao accountUserDao;
    private final AccountRoleDao accountRoleDao;
//    private final UserMapper userMapper;
    public final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationCodeDao confirmationCodeDao;
    private final JanitorImageDao janitorImageDao;
    private final JanitorService janitorService;
//    private final ParticipantMapper participantMapper;

//    @Autowired
//    @Lazy
//    public void setUserService(JpaUserDetailService jpaUserDetailService){
//        this.userSevice = JpaUserDetailService;
//    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        return accountUserDao.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username: " + username + " not found")
        );

    }

    @Override
    public String getConfirmationCode() {
        String confirmationCode;
        return confirmationCode = RandomStringUtils.randomNumeric(5);
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (accountUserDao.findByUsername(userDto.getUsername()).isPresent()) {
            throw  new UsernameAlreadyExistsException(String.format(
                    "Пользователь с таким логином %s уже существует", userDto.getUsername()));
        }
        AccountUser accountUser = modelMapper.map(userDto, AccountUser.class);
//        AccountUser accountUser = userMapper.toAccountUser(userDto);
        Janitor janitor = addNewPlayer(accountUser);
//        player.setRating(BigDecimal.valueOf(500));

        janitorService.save(janitor);
        JanitorImage janitorImage = addNewImage(imageName, janitor);
        janitorImageDao.save(janitorImage);
        AccountRole roleUser = accountRoleDao.findByName("ROLE_USER");
        AccountRole roleAdmin = accountRoleDao.findByName("ROLE_ADMIN");
        AccountRole rolePlayer = accountRoleDao.findByName("ROLE_PLAYER");
        long count = accountUserDao.count();
        if(count == 0){
            accountUser.setRoles(Set.of(roleAdmin));
        } else accountUser.setRoles(Set.of(roleUser));
        log.info("Count: " + count);
        log.info(String.valueOf(count == 0));
        accountUser.setStatus(AccountStatus.ACTIVE);
        accountUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        accountUser.setJanitor(janitor);
        AccountUser registeredAccountUser = accountUserDao.save(accountUser);
        log.debug("User with username {} was registered successfully", registeredAccountUser.getUsername());
        return modelMapper.map(registeredAccountUser, UserDto.class);

    }

    public JanitorImage addNewImage(String nameImage, Janitor janitor){
        JanitorImage janitorImage = new JanitorImage();
        if (janitorImageDao.count() != 0){
            janitorImage.setId(janitorImageDao.maxId() + 1);
        }
        janitorImage.setPath(nameImage);
        janitorImage.setJanitor(janitor);
        return janitorImage;
    }

    public Janitor addNewPlayer(AccountUser accountUser){
        Janitor janitor = modelMapper.map(accountUser, Janitor.class);
        if (janitorService.count() != 0){
            janitor.setId(janitorService.maxId() + 1);
        }
        janitor.setStatus(Status.NOT_ACTIVE);
        return janitor;
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        AccountUser user = modelMapper.map(userDto, AccountUser.class);
        if (user.getId() != null) {
            accountUserDao.findById(userDto.getId()).ifPresent(
                    (p) -> {
                        user.setVersion(p.getVersion());
                        user.setStatus(p.getStatus());
                    }
            );
        }
        return modelMapper.map(accountUserDao.save(user), UserDto.class);

    }

    @Override
    public AccountUser findByUsername(String username) {
        return accountUserDao.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username: " + username + " not found")
        );
    }
    public AccountUser update(AccountUser accountUser) {
        if (accountUser.getId() != null) {
            accountUserDao.findById(accountUser.getId()).ifPresent(
                    (user) -> accountUser.setVersion(user.getVersion())
            );
        }
        return accountUserDao.save(accountUser);
    }

//    @Override
//    public UserDto findById(Long id) {
//        return null;
//    }

    @Override
    public List<UserDto> findAll() {
        return null;
    }

    @Override
    public void generateConfirmationCode(UserDto thisUser, String code) {
        ConfirmationCode confirmationCode = ConfirmationCode.builder().
                code(code)
                .accountUser(modelMapper.map(thisUser, AccountUser.class))
                .build();
        confirmationCodeDao.save(confirmationCode);
    }
//            userMapper.toAccountUser(thisUser)
    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return modelMapper.map(accountUserDao.findById(id).orElse(null), UserDto.class);
//        return userMapper.toUserDto(accountUserDao.findById(id).orElse(null));
    }

//    @Override
//    public List<UserDto> findAll() {
//        return accountUserDao.findAll().stream()
//                .map(userMapper::toUserDto)
//                .collect(Collectors.toList());
//    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        final AccountUser accountUser = accountUserDao.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User with id %s not found", id)
                )
        );
        disable(accountUser);
        update(accountUser);
    }

    private void enable(final AccountUser accountUser) {
        accountUser.setStatus(AccountStatus.ACTIVE);
        accountUser.setAccountNonLocked(true);
        accountUser.setAccountNonExpired(true);
        accountUser.setEnabled(true);
        accountUser.setCredentialsNonExpired(true);
    }

    private void disable(final AccountUser accountUser) {
        accountUser.setStatus(AccountStatus.DELETED);
        accountUser.setAccountNonLocked(false);
        accountUser.setAccountNonExpired(false);
        accountUser.setEnabled(false);
        accountUser.setCredentialsNonExpired(false);
    }
}
