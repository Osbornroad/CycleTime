package com.gmail.osbornroad.cycletime.service;

import com.gmail.osbornroad.cycletime.dao.FakeProcessDaoImpl;
import com.gmail.osbornroad.cycletime.dao.ProcessDao;
import com.gmail.osbornroad.cycletime.model.Process;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 27.04.2017.
 */

public class FakeProcessServiceImpl implements ProcessService {

    private ProcessDao processDao;

    public FakeProcessServiceImpl(ProcessDao processDao) {
        this.processDao = processDao;
    }

    @Override
    public Process get(int id) {
        return processDao.get(id);
    }

    @Override
    public boolean save(Process process) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Process> getAll() {
        return processDao.getAll();
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printAll() {
        List<Process> listForPrinting = getAll();
        Collections.sort(listForPrinting, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                return o1.getId() - o2.getId();
            }
        });
        System.out.println();
        for (Process process : listForPrinting) {
            System.out.println(process);
        }
        System.out.println();
        System.out.println("*********************************************************");
    }

    @Override
    public Process[] getProcessArray() {
        return getAll().toArray(new Process[getAll().size()]);
    }

    public static void main(String[] args) {
        ProcessService processService = new FakeProcessServiceImpl(new FakeProcessDaoImpl());

        processService.printAll();
        System.out.println(processService.get(202));
    }
}
