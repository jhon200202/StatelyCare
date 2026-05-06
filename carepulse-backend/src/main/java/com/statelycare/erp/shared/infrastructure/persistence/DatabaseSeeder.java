package com.statelycare.erp.shared.infrastructure.persistence;

import com.statelycare.erp.auth.domain.model.Role;
import com.statelycare.erp.auth.infrastructure.persistence.SpringDataUserRepository;
import com.statelycare.erp.auth.infrastructure.persistence.UserEntity;
import com.statelycare.erp.room.domain.model.Room;
import com.statelycare.erp.room.domain.model.RoomType;
import com.statelycare.erp.room.domain.model.Wing;
import com.statelycare.erp.room.domain.repository.RoomRepository;
import com.statelycare.erp.employee.domain.model.CertificationStatus;
import com.statelycare.erp.employee.domain.model.Department;
import com.statelycare.erp.employee.domain.model.Employee;
import com.statelycare.erp.employee.domain.repository.EmployeeRepository;
import com.statelycare.erp.resident.domain.model.Gender;
import com.statelycare.erp.resident.domain.model.Resident;
import com.statelycare.erp.resident.domain.model.ResidentStatus;
import com.statelycare.erp.resident.domain.repository.ResidentRepository;
import com.statelycare.erp.attendance.domain.model.AttendanceRecord;
import com.statelycare.erp.attendance.domain.model.AttendanceStatus;
import com.statelycare.erp.attendance.domain.repository.AttendanceRepository;
import com.statelycare.erp.treatment.domain.model.*;
import com.statelycare.erp.treatment.domain.repository.MedicationAdministrationRepository;
import com.statelycare.erp.treatment.domain.repository.TreatmentRepository;
import com.statelycare.erp.finance.domain.model.*;
import com.statelycare.erp.finance.domain.repository.FinancialTransactionRepository;
import com.statelycare.erp.nutrition.domain.model.DailyMenu;
import com.statelycare.erp.nutrition.domain.model.MealType;
import com.statelycare.erp.nutrition.domain.model.MenuItem;
import com.statelycare.erp.nutrition.domain.model.TextureModification;
import com.statelycare.erp.nutrition.domain.repository.DailyMenuRepository;
import com.statelycare.erp.nutrition.domain.repository.MenuItemRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final SpringDataUserRepository userRepository;
    private final RoomRepository roomRepository;
    private final EmployeeRepository employeeRepository;
    private final ResidentRepository residentRepository;
    private final AttendanceRepository attendanceRepository;
    private final TreatmentRepository treatmentRepository;
    private final MedicationAdministrationRepository medicationAdministrationRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final MenuItemRepository menuItemRepository;
    private final DailyMenuRepository dailyMenuRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(
            SpringDataUserRepository userRepository,
            RoomRepository roomRepository,
            EmployeeRepository employeeRepository,
            ResidentRepository residentRepository,
            AttendanceRepository attendanceRepository,
            TreatmentRepository treatmentRepository,
            MedicationAdministrationRepository medicationAdministrationRepository,
            FinancialTransactionRepository financialTransactionRepository,
            MenuItemRepository menuItemRepository,
            DailyMenuRepository dailyMenuRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.employeeRepository = employeeRepository;
        this.residentRepository = residentRepository;
        this.attendanceRepository = attendanceRepository;
        this.treatmentRepository = treatmentRepository;
        this.medicationAdministrationRepository = medicationAdministrationRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.menuItemRepository = menuItemRepository;
        this.dailyMenuRepository = dailyMenuRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedAdminUser();
        }
        
        if (roomRepository.findAll().isEmpty()) {
            seedData();
        }
    }

    private void seedAdminUser() {
        UserEntity admin = new UserEntity();
        admin.setId(UUID.randomUUID());
        admin.setUsername("admin");
        admin.setEmail("admin@carepulse.com");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        admin.setCreatedAt(Instant.now());
        admin.setUpdatedAt(Instant.now());

        userRepository.save(admin);
        System.out.println(">>> Database seeded: Admin user created");
    }

    private void seedData() {
        Random random = new Random();
        String[] firstNames = {"Juan", "Maria", "Pedro", "Ana", "Luis", "Elena", "Carlos", "Sofia", "Miguel", "Lucia", "Jose", "Carmen", "Javier", "Laura", "Diego", "Paula", "Andres", "Isabel", "Fernando", "Rosa"};
        String[] lastNames = {"Garcia", "Martinez", "Lopez", "Sanchez", "Perez", "Gomez", "Rodriguez", "Fernandez", "Moreno", "Jimenez", "Ruiz", "Hernandez", "Diaz", "Alvarez", "Torres", "Vidal", "Moya", "Castro", "Ortiz", "Bravo"};

        // 1. Rooms
        List<Room> rooms = new ArrayList<>();
        Wing[] wings = Wing.values();
        RoomType[] roomTypes = RoomType.values();
        for (int i = 0; i < 10; i++) {
            Room r = Room.createNew("R" + (100 + i), wings[random.nextInt(wings.length)], (i/5)+1, 2, roomTypes[random.nextInt(roomTypes.length)]);
            rooms.add(roomRepository.save(r));
        }

        // 2. Employees (and their Users)
        List<Employee> employees = new ArrayList<>();
        Department[] depts = Department.values();
        for (int i = 0; i < 20; i++) {
            // Create User first
            UserEntity user = new UserEntity();
            user.setId(UUID.randomUUID());
            user.setUsername("employee" + i);
            user.setEmail("emp" + i + "@carepulse.com");
            user.setPasswordHash(passwordEncoder.encode("pass123"));
            user.setRole(Role.STAFF);
            user.setActive(true);
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            userRepository.save(user);

            Employee e = Employee.createNew(
                user.getId(),
                "EMP-" + (1000 + i),
                firstNames[i % firstNames.length],
                lastNames[i % lastNames.length],
                depts[random.nextInt(depts.length)],
                "Professional " + (i+1),
                LocalDate.now().minusYears(1 + random.nextInt(5))
            );
            employees.add(employeeRepository.save(e));
        }

        // 3. Residents
        List<Resident> residents = new ArrayList<>();
        Gender[] genders = Gender.values();
        for (int i = 0; i < 20; i++) {
            Room randomRoom = rooms.get(random.nextInt(rooms.size()));
            Employee randomDoc = employees.get(random.nextInt(employees.size()));
            
            Resident res = Resident.createNew(
                "RES-" + (5000 + i),
                firstNames[random.nextInt(firstNames.length)],
                lastNames[random.nextInt(lastNames.length)],
                LocalDate.now().minusYears(70 + random.nextInt(20)),
                genders[random.nextInt(genders.length)],
                randomRoom.id(),
                LocalDate.now().minusMonths(random.nextInt(24))
            );
            // Link Physician
            Resident savedRes = residentRepository.save(res);
            residents.add(savedRes);
        }

        // 4. Attendance
        for (Employee e : employees) {
            attendanceRepository.save(AttendanceRecord.checkIn(e.id(), LocalTime.of(8, 0), LocalTime.of(16, 0)));
        }

        // 5. Treatments & 6. Administrations
        for (Resident r : residents) {
            Treatment t = Treatment.createNew(
                r.id(),
                employees.get(random.nextInt(employees.size())).id(),
                "Treatment for " + r.firstName(),
                TreatmentType.MEDICATION,
                Frequency.DAILY,
                LocalDate.now().minusDays(5)
            );
            Treatment savedT = treatmentRepository.save(t);
            
            medicationAdministrationRepository.save(MedicationAdministration.schedule(
                savedT.id(), 
                Instant.now().plusSeconds(3600), 
                MedicationRoute.ORAL
            ));
        }

        // 7. Financial Transactions
        TransactionCategory[] categories = TransactionCategory.values();
        for (int i = 0; i < 20; i++) {
            FinancialTransaction ft = FinancialTransaction.createNew(
                "TX-" + (9000 + i),
                i % 2 == 0 ? TransactionType.INCOME : TransactionType.EXPENSE,
                categories[random.nextInt(categories.length)],
                new BigDecimal(500 + random.nextInt(2000)),
                PaymentMethod.BANK_TRANSFER,
                LocalDate.now().minusDays(random.nextInt(30))
            );
            financialTransactionRepository.save(ft);
        }

        // 8. Menu Items
        List<MenuItem> menuItems = new ArrayList<>();
        MealType[] mealTypes = MealType.values();
        TextureModification[] textures = TextureModification.values();
        for (int i = 0; i < 20; i++) {
            MenuItem mi = MenuItem.create(
                "Food Item " + (i+1),
                "Description for item " + (i+1),
                mealTypes[random.nextInt(mealTypes.length)],
                textures[random.nextInt(textures.length)]
            );
            menuItems.add(menuItemRepository.save(mi));
        }

        // 9. Daily Menus
        for (int i = 0; i < 5; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            for (MealType mt : mealTypes) {
                dailyMenuRepository.save(DailyMenu.create(
                    date,
                    menuItems.get(random.nextInt(menuItems.size())).id(),
                    mt,
                    50,
                    null
                ));
            }
        }

        System.out.println(">>> Database seeded: 20 records per table (MySQL 8 Schema)");
    }
}
