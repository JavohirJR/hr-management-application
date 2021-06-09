package ecma.ai.hrapp.service;

import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.TurniketHistory;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TurniketHistoryDto;
import ecma.ai.hrapp.repository.TuniketRepository;
import ecma.ai.hrapp.repository.TurniketHistoryRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TurniketHistoryService {

    @Autowired
    TuniketRepository turniketRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TurniketHistoryRepository turniketHistoryRepository;

    public ApiResponse add(TurniketHistoryDto turniketHistoryDto) {
        Optional<Turniket> optionalTurniket = turniketRepository.findByNumber(turniketHistoryDto.getNumber());
        if (!optionalTurniket.isPresent()) {
            return new ApiResponse("Karochchi brat ruxsati yo'q joyga kirmoqchi bo'lyapti! Turniket topilmadi!", false);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User owner = optionalUser.get();
        Set<Role> roles = owner.getRoles();
        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))
                || owner.getPosition().contains("hr".toUpperCase())) {
            TurniketHistory turniketHistory = new TurniketHistory();
            turniketHistory.setTurniket(optionalTurniket.get());
            turniketHistory.setType(turniketHistoryDto.getType());
            turniketHistoryRepository.save(turniketHistory);
            return new ApiResponse("Yo'liz ochiq, bemalol kirishiz mumkin!", true);
        }
        return new ApiResponse("Karochchi brat ruxsatiz yo'q joyga kirmoqchi bo'lyapsiz! Sizda huquq yo'q!", false);
    }

    public ApiResponse getAllByDate(String number, Timestamp startTime, Timestamp endTime) {
        Optional<Turniket> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("Karochchi brat ruxsati yo'q joyga kirmoqchi bo'lyapti! Bunaqa turniket yoq.", false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User owner = optionalUser.get();
        Set<Role> roles = owner.getRoles();
        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))
                || owner.getPosition().contains("hr".toUpperCase())) {

            List<TurniketHistory> historyList = turniketHistoryRepository.findAllByTurniketAndTimeIsBetween(optionalTurniket.get(), startTime, endTime);
            return new ApiResponse("History list by date", true, historyList);

        }
        return new ApiResponse("Karochchi brat ruxsatiz yo'q joyga kirmoqchi bo'lyapsiz! Sizda huquq yo'q!", false);
    }

    public ApiResponse getAll(String number) {
        Optional<Turniket> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("Karochchi brat ruxsati yo'q joyga kirmoqchi bo'lyapti! Bunaqa turniket yoq.", false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User owner = optionalUser.get();
        Set<Role> roles = owner.getRoles();
        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))
                || owner.getPosition().contains("hr".toUpperCase())) {
            List<TurniketHistory> allByTurniket = turniketHistoryRepository.findAllByTurniket(optionalTurniket.get());
            return new ApiResponse("All history by turniket!", true, allByTurniket);
        }
        return new ApiResponse("Karochchi brat ruxsatiz yo'q joyga kirmoqchi bo'lyapsiz! Sizda huquq yo'q!", false);
    }
}
