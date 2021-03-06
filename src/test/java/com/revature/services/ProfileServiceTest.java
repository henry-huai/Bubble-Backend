package com.revature.services;

import com.revature.models.Profile;
import com.revature.repositories.ProfileRepo;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProfileServiceTest {
    private static final String USERNAME = "dummyUsername";
    private static final String PASSWORD = "abc123";
    private static final String EMAIL = "dummy@email.com";
    private Profile expected = new Profile();

    @Mock
    ProfileRepo profileRepo;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void initMock() {
        MockitoAnnotations.openMocks(this);
        String name = "dummyName";
        String passkey = "c8ZLBnfDh3YsvZ2dW1KDWY6ZTak8+v+/L74e8Vfoydk1IfySsVCAZVKTZfrtPKodzUXEiR+69yjOz1qqf7U4rA==jnW" +
                "2sIxW7inUlQqGJCNrNa7Eavj5uMGQAYZ0S6xNz65p79QaOk8eZpOChJlFPvIadohhOuHg5PFGeewM2YmkVR260YPhJwK/GUR3YXs" +
                "UH0+KjOQKuHAHY8CyLwpBsNV0DsPO56jn2As1CfBMJc9VSNsx37W4Vo5MCaOTisZwBWjUsTG9i+HecqcK9C0tIWC2Jn44oX5BAS3" +
                "1Huev/ZIWf2OE3FjHSMLyJs0TmJAPxBg6IllwEhv75+ffGbZENZVkkHV3LRxUWKtGdQa5tSOt6pdzIZtdSO6o81zXD6BBDfEZo22" +
                "qCVCR2C2DQbsmaMJhInHwxEZ3RS3/1N+rz85STq/h+nKvqBcoPOFfq0o5tRRnOlRVFpWuONhiY7IUSqtTZCJmqHnALSRFJwMGFPj" +
                "mUkq1nQxux1rgxllacVb+AT4bS+Xbw8DyUwGepmTCiw4t75krGyCSBArcmfiFBtgDkVZTFIJ+GMFhFbpWv2darLcxKlgSdur/z9Y" +
                "CYoZcKm9vrrH+CaFykfIUdjnln5jhLoRmjeBIHgYWITG5J5/NCzAM+a3k4Y92/hbgDDE15GD1ud1EU8GHY4eb5LU1pAb2O7zbcW9" +
                "pQbtVcbqyJGNRFA6OAGcWb1R0+04d0+1DA6BjTDsxkltgsvUpLrVFBo4VaFAT6Jf4ZI2Pg39WjFY1an8=";
        expected = new Profile(USERNAME, passkey, name, name, EMAIL);
    }

    @Test
    void testLoginSuccess(){
        when(profileRepo.getProfileByUsername(USERNAME)).thenReturn(expected);
        Profile actual = profileService.login(USERNAME, PASSWORD);
        assertEquals(expected,actual);
    }

    @Test
    void testLoginNullEmail(){
        when(profileRepo.getProfileByEmail(null)).thenReturn(null);
        Profile actual = profileService.login(null, PASSWORD);
        assertNull(actual);
    }

    @Test
    void testLoginNullPass(){
        when(profileRepo.getProfileByEmail(EMAIL)).thenReturn(expected);
        Profile actual = profileService.login(EMAIL,null);
        assertNull(actual);
    }

    @Test
    void testLoginBadEmail(){
        when(profileRepo.getProfileByEmail("banana")).thenReturn(null);
        Profile actual = profileService.login("banana","tomato");
        assertNull(actual);
    }

    @Test
    void testLoginBadPass(){
        when(profileRepo.getProfileByEmail(EMAIL)).thenReturn(expected);
        Profile actual = profileService.login(EMAIL,"tomato");
        assertNull(actual);
    }

    @Test
    void testFindProfileByEmailSuccess(){
        when(profileRepo.getProfileByEmail(EMAIL)).thenReturn(expected);
        Profile actual = profileRepo.getProfileByEmail(EMAIL);
        assertEquals(actual,expected);
    }

    @Test
    void testFindProfileByEmailNullEntry(){
        when(profileRepo.getProfileByEmail(null)).thenReturn(null);
        Profile actual = profileRepo.getProfileByEmail((null));
        assertNull(actual);
    }

    @Test
    void testFindProfileByEmailBadEntry(){
        when(profileRepo.getProfileByEmail("FloppyDisk")).thenReturn(null);
        Profile actual = profileRepo.getProfileByEmail(("FloppyDisk"));
        assertNull(actual);
    }

    @Test
    void addNewProfile() {
        when(profileRepo.save(expected)).thenReturn(expected);
        assertEquals(expected, profileService.addNewProfile(expected));
    }

    @Test
    void getProfileByUser() {
        when(profileRepo.getProfileByEmail(expected.getEmail())).thenReturn(expected);
        Profile actual = profileService.getProfileByEmail(expected);
        assertEquals(expected,actual);
    }

    @Test
    void testGetExistingProfile() {
        when(profileRepo.getProfileByPid(1)).thenReturn(expected);
        assertEquals(expected, profileService.getProfileByPid(1));
    }

    @Test
    void testGetInvalidProfile() {
        when(profileRepo.getProfileByPid(1)).thenReturn(null);
        assertNull(profileService.getProfileByPid(1));
    }

    @Test
    void testUpdateExistingProfile() {
        when(profileRepo.getProfileByPid(expected.getPid())).thenReturn(expected);
        when(profileRepo.save(expected)).thenReturn(expected);
        assertEquals(this.expected, profileService.updateProfile(this.expected));
    }

    @Test
    void testUpdateInvalidProfile() {
        when(profileRepo.getProfileByPid(expected.getPid())).thenReturn(null);
        when(profileRepo.save(null)).thenReturn(null);
        assertNull(profileService.updateProfile(expected));
    }

    @Test
    public void testAddFollowerByEmail(){
        ArrayList<Profile> empty = new ArrayList<>();
        Profile profile = new Profile(1,"test","1234","updateTest","updateTest","test@mail", empty);
        Profile profile2 = new Profile(2,"test2","1234","updateTest2","updateTest2","test2@mail", empty);

        ArrayList<Profile> followed = new ArrayList<>();
        followed.add(profile2);
        Profile expected = new Profile(1, "test", "1234", "updateTest", "updateTest", "test@mail", followed);

        when(profileRepo.getProfileByEmail("test2@mail")).thenReturn(profile2);
        Profile result = profileService.addFollowerByEmail(profile, profile2.getEmail());

        assertEquals(expected, result);
    }


    @Test
    public void testDeleteFollowerByEmail(){
        ArrayList<Profile> empty = new ArrayList<>();
        Profile profile = new Profile(1,"test","1234","updateTest","updateTest","test@mail", empty);
        Profile profile2 = new Profile(2,"test2","1234","updateTest2","updateTest2","test2@mail", empty);

        Profile expected = new Profile(1, "test", "1234", "updateTest", "updateTest", "test@mail", empty);

        when(profileRepo.getProfileByEmail("tes2@mail")).thenReturn(profile);
        when(profileRepo.getProfileByEmail("test2@mail")).thenReturn(profile2);

        profile = profileService.addFollowerByEmail(profile, profile2.getEmail());

        Profile result = profileService.removeFollowByEmail(profile, profile2.getEmail());

        assertEquals(expected, result);
    }
}
