package com.emis_app.emis_app.seeder;

import com.emis_app.emis_app.entity.School;
import com.emis_app.emis_app.entity.Learner;
import com.emis_app.emis_app.repository.SchoolRepository;
import com.emis_app.emis_app.repository.LearnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private LearnerRepository learnerRepository;

    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (schoolRepository.count() == 0) {
            seedDatabase();
        }
    }

    private void seedDatabase() {
        List<School> schools = createSchools();
        schoolRepository.saveAll(schools);

        List<Learner> learners = createLearners(schools);
        learnerRepository.saveAll(learners);

        System.out.println("Database seeded successfully!");
        System.out.println("Created " + schools.size() + " schools and " + learners.size() + " learners");
    }

    private List<School> createSchools() {
        List<School> schools = new ArrayList<>();

        String[] schoolNames = {
                "Kampala International School", "Makerere College School", "St. Mary's College Kisubi",
                "King's College Budo", "Gayaza High School", "Namagunga Boarding School",
                "Lubiri Secondary School", "Mengo Senior School", "Kitante Primary School",
                "Nakasero Primary School", "Bugolobi Flats Primary", "Kololo Senior Secondary",
                "Muyenga Tank Hill School", "Ntinda View College", "Kansanga Miracle Centre",
                "Banda Parents School", "Kyambogo College School", "Mulago Primary School",
                "Wandegeya Primary School", "Katwe Primary School"
        };

        String[] schoolTypes = {
                "Public Primary", "Private Primary", "Public Secondary", "Private Secondary",
                "Government Aided", "International School"
        };

        String[] locations = {
                "Kampala Central Division", "Kawempe Division", "Makindye Division",
                "Nakawa Division", "Rubaga Division", "Kololo, Kampala", "Bugolobi, Kampala",
                "Muyenga, Kampala", "Ntinda, Kampala", "Kansanga, Kampala", "Banda, Kampala",
                "Kyambogo, Kampala", "Mulago, Kampala", "Wandegeya, Kampala", "Katwe, Kampala",
                "Mengo, Kampala", "Lubiri, Kampala", "Gayaza, Wakiso", "Kisubi, Wakiso",
                "Budo, Wakiso"
        };

        for (int i = 0; i < 20; i++) {
            School school = new School();
            school.setName(schoolNames[i]);
            school.setSchoolType(schoolTypes[random.nextInt(schoolTypes.length)]);
            school.setLocation(locations[i]);
            school.setEnrollmentCapacity(random.nextInt(1500) + 200); // 200-1700 capacity
            schools.add(school);
        }

        return schools;
    }

    private List<Learner> createLearners(List<School> schools) {
        List<Learner> learners = new ArrayList<>();

        String[] maleNames = {
                "James Okello", "David Musoke", "Samuel Kato", "Emmanuel Ssebunya", "Isaac Mukasa",
                "Patrick Namugga", "Joseph Wasswa", "Daniel Kiprotich", "Michael Asiimwe", "Andrew Tumusiime",
                "Brian Sekandi", "Felix Mubiru", "Paul Wanyama", "Timothy Nakato", "Joshua Kyeyune",
                "Robert Kabaale", "Simon Lubega", "Peter Kalungi", "Francis Ssemwogerere", "Anthony Kiggundu"
        };

        String[] femaleNames = {
                "Sarah Namukasa", "Grace Nalwanga", "Maria Nakamya", "Rebecca Namusoke", "Ruth Akello",
                "Joy Nalubega", "Faith Namatovu", "Mercy Nakigozi", "Patience Nabirye", "Hope Nassozi",
                "Esther Nakalembe", "Priscilla Namubiru", "Lydia Nakawuki", "Agnes Namusisi", "Joan Nankya",
                "Catherine Namazzi", "Betty Namutebi", "Susan Nakibuuka", "Rachel Nambi", "Winnie Nakirya"
        };

        String[] grades = {
                "P1", "P2", "P3", "P4", "P5", "P6", "P7",
                "S1", "S2", "S3", "S4", "S5", "S6"
        };

        String[] academicYears = {
                "2023-2024", "2024-2025"
        };

        String[] genders = {"Male", "Female"};

        for (int i = 0; i < 20; i++) {
            Learner learner = new Learner();

            String gender = genders[random.nextInt(genders.length)];
            learner.setGender(gender);

            if (gender.equals("Male")) {
                learner.setName(maleNames[random.nextInt(maleNames.length)]);
            } else {
                learner.setName(femaleNames[random.nextInt(femaleNames.length)]);
            }

            learner.setGrade(grades[random.nextInt(grades.length)]);
            learner.setAcademicYear(academicYears[random.nextInt(academicYears.length)]);
            learner.setSchool(schools.get(random.nextInt(schools.size())));

            learners.add(learner);
        }

        return learners;
    }
}