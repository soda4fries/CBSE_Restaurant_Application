package com.cbse.restaurant.domain;

import static com.cbse.restaurant.domain.StaffTestSamples.*;
import static com.cbse.restaurant.domain.StaffTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cbse.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StaffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Staff.class);
        Staff staff1 = getStaffSample1();
        Staff staff2 = new Staff();
        assertThat(staff1).isNotEqualTo(staff2);

        staff2.setId(staff1.getId());
        assertThat(staff1).isEqualTo(staff2);

        staff2 = getStaffSample2();
        assertThat(staff1).isNotEqualTo(staff2);
    }

    @Test
    void managerTest() {
        Staff staff = getStaffRandomSampleGenerator();
        Staff staffBack = getStaffRandomSampleGenerator();

        staff.setManager(staffBack);
        assertThat(staff.getManager()).isEqualTo(staffBack);

        staff.manager(null);
        assertThat(staff.getManager()).isNull();
    }
}
