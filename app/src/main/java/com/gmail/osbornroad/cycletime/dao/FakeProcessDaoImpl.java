package com.gmail.osbornroad.cycletime.dao;

import com.gmail.osbornroad.cycletime.model.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by User on 27.04.2017.
 */

public class FakeProcessDaoImpl implements ProcessDao {

    private List<Process> processList;
    private AtomicInteger idCounter = new AtomicInteger(200);
    private String[] processNamesArray;

    public FakeProcessDaoImpl() {
        this.processList = new ArrayList<>();
        populateProcessList();
    }

    private void populateProcessList() {
        processNamesArray = new String[]{
                "HPC",
                "Лазер",
                "Снятие фаски",
                "Зачистка",
                "Уменьшение диаметра",
                "Формовка",
                "PCO",
                "Нарезка SUMI",
                "Печь",
                "Tokai",
                "Гибка",
                "Сборка"
        };
        for (String processName : processNamesArray) {
            processList.add(new Process(getNewId(), processName, true, new ArrayList<Integer>()));
        }
    }

    private int getNewId() {
        return idCounter.getAndIncrement();
    }

    @Override
    public Process get(int id) {
        for (Process process : processList) {
            if (id == process.getId()) {
                return process;
            }
        }
        return null;
    }

    @Override
    public boolean save(Process process) {
        return false;
    }

    @Override
    public List<Process> getAll() {
        return processList;
    }

    @Override
    public void delete(int id) {

    }
}
