package com.gmail.osbornroad.cycletime.dao;

import android.content.res.Resources;

import com.gmail.osbornroad.cycletime.model.Sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by User on 24.04.2017.
 */

public class FakeSampleDaoImpl implements SampleDao {

    private List<Sample> sampleList;

    public FakeSampleDaoImpl() {
        this.sampleList = new ArrayList<Sample>();
        populateSampleList();
    }

    private void populateSampleList() {
        sampleList.add(new Sample(new GregorianCalendar(2017, 3, 5), "HPC", "HPC", "3067", "Макаров", 17, null));
        sampleList.add(new Sample(new GregorianCalendar(2017, 3, 6), "Endforming", "HPC", "3067", "Макаров", 17, null));
        sampleList.add(new Sample(new GregorianCalendar(2017, 3, 7), "Bending", "HPC", "3067", "Макаров", 17, null));
        sampleList.add(new Sample(new GregorianCalendar(2017, 4, 5), "Laser", "HPC", "3067", "Макаров", 17, null));
        sampleList.add(new Sample(new GregorianCalendar(2017, 4, 10), "Bending", "HPC", "3067", "Макаров", 17, null));
        sampleList.add(new Sample(new GregorianCalendar(2017, 4, 12), "HPC", "HPC", "3067", "Макаров", 17, null));
    }

    @Override
    public void createSample(Sample sample) {

    }

    @Override
    public Collection<Sample> getAll() {
        return null;
    }

    @Override
    public Collection<Sample> getFiltered(GregorianCalendar startDate, GregorianCalendar endDate, String processName, String machineName, String partName, String workerName) {
        return null;
    }

    @Override
    public void deleteSample(int id) {

    }

    @Override
    public Sample updateSample(Sample sample) throws Resources.NotFoundException {
        return null;
    }
}
