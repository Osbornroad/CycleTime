package com.gmail.osbornroad.cycletime.service;

import android.content.res.Resources;

import com.gmail.osbornroad.cycletime.model.Sample;

import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * Created by User on 24.04.2017.
 */

public interface sampleService {
    void createSample(Sample sample);

    Collection<Sample> getAll();

    Collection<Sample> getFiltered(GregorianCalendar startDate,
            GregorianCalendar endDate,
            String processName,
            String machineName,
            String partName,
            String workerName);

    void deleteSample(int id);

    Sample updateSample(Sample sample) throws Resources.NotFoundException;

}
