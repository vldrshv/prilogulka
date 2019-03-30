package com.example.prilogulka;

import com.example.prilogulka.data.managers.CoefficientManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoefficientManagerTest {
    CoefficientManager cm;

    // 18-25 1.0
    // 26-35 1.1
    // 36-45 1.0
    // 46-100 1.2

    @Before
    public void init(){
        cm = new CoefficientManager();
    }
    @Test
    public void test10 (){
        Assert.assertEquals(cm.getAgeCoefficient(10), 1.0, 0.0);
    }
    @Test
    public void test20 (){
        Assert.assertEquals(cm.getAgeCoefficient(20), 1.0, 0.0);
    }
    @Test
    public void test30 (){
        Assert.assertEquals(cm.getAgeCoefficient(30), 1.1, 0.0);
    }
    @Test
    public void test40 (){
        Assert.assertEquals(cm.getAgeCoefficient(40), 1.0, 0.0);
    }
    @Test
    public void test50 (){
        Assert.assertEquals(cm.getAgeCoefficient(50), 1.2, 0.0);
    }
    @Test
    public void test60 (){
        Assert.assertEquals(cm.getAgeCoefficient(60), 1.2, 0.0);
    }
    @Test
    public void test70 (){
        Assert.assertEquals(cm.getAgeCoefficient(70), 1.2, 0.0);
    }
    @Test
    public void test80 (){
        Assert.assertEquals(cm.getAgeCoefficient(80), 1.2, 0.0);
    }
    @Test
    public void test90 (){
        Assert.assertEquals(cm.getAgeCoefficient(90), 1.2, 0.0);
    }
    @Test
    public void test100 (){
        Assert.assertEquals(cm.getAgeCoefficient(100), 1.2, 0.0);
    }

    @Test
    public void test18_25() {
        for (int i = 18; i <= 25; i++)
        Assert.assertEquals(cm.getAgeCoefficient(i), 1.0, 0.0);
    }
    @Test
    public void test26_35() {
        for (int i = 26; i <= 35; i++)
            Assert.assertEquals(cm.getAgeCoefficient(i), 1.1, 0.0);
    }
    @Test
    public void test36_45() {
        for (int i = 36; i <= 45; i++)
            Assert.assertEquals(cm.getAgeCoefficient(i), 1.0, 0.0);
    }
    @Test
    public void test46_100() {
        for (int i = 46; i <= 100; i++)
            Assert.assertEquals(cm.getAgeCoefficient(i), 1.2, 0.0);
    }
}
