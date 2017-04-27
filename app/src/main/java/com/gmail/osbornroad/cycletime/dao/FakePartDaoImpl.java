package com.gmail.osbornroad.cycletime.dao;

import com.gmail.osbornroad.cycletime.model.Part;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by User on 27.04.2017.
 */

public class FakePartDaoImpl implements PartDao {

    private List<Part> partList;
    private AtomicInteger idCounter = new AtomicInteger(400);
    private String[] partNameArray;

    public FakePartDaoImpl() {
        this.partList = new ArrayList<>();
        populatePartList();
    }

    private void populatePartList() {
        partNameArray = new String[]{
                "322 x 11008CT  [47401-5BF0A]",
                "479 x 3100PC0  [17510-EM91A]",
                "557 x 2047NE0  [46283-4CM0A]",
                "580 x 20478ET  [46240-5BK0A]",
                "588 x 20478ET  [46240-5BF0A]",
                "604 x 2047NE0  [46282-4CM0A]",
                "752 x 10639C0  [17339-EM92A]",
                "759 x 3100PC0  [17510-4CM2C]",
                "959 x 20638ET  [46250-5BK0A]",
                "976 x 20638ET  [46250-5BF0A]",
                "1040 x 20478ET  [46283-5BF0A]",
                "1047 x 20638ET  [46252-5BF0A]",
                "1054 x 20478ET  [46282-5BK0A]",
                "1068 x 20478ET  [46282-5BF0A]",
                "1086 x 20638ET  [46252-5BK0A]",
                "1109 x 2047NE0  [46240-4CM0A]",
                "1122 x 20478ET  [46283-5BK0A]",
                "1143 x 10639C0  [17339-4CM0A]",
                "1364 x 2063NE0  [46250-4CM0A]",
                "1371 x 20478ET  [46242-5BK0A]",
                "1430 x 20478ET  [46242-5BF0A]",
                "1453 x 2063NE0  [46252-4CM0A]",
                "1952 x 2047NE0  [46242-4CM0A]",
                "2385 x 3080PC0  [17506-EM92A]",
                "2389 x 3080PC0  [17506-EM90A]",
                "2450 x 3080PC0  [17506-4CM0A]",
                "2504 x 3100PC0  [17506-EM91A]",
                "2512 x 3080PC0  [17506-5BF0A]",
                "2552 x 3080PC0  [17506-5BK0A]",
                "2560 x 3080PC0  [17506-4CM1A]",
                "2564 x 3100PC0  [17506-4CM2A]",
                "2800 x 3100PC0  [17510-EM91C]",
                "3007 x 2047PED  [46284-EM90A]",
                "3008 x 2047PED  [46284-EM92A]",
                "3067 x 2047PE0  [46284-4CM0A]",
                "3169 x 2047PE0  [46285-5BF0A]",
                "3188 x 3100PC0  [17510-EM91B]",
                "3549 x 3100HC0  [17510-4CM2A]",
                "3707 x 10639C0  [17338-5BF0A]",
                "3707 x 10639C1  [17338-5BF0A]",
                "3759 x 10639C0  [17338-5BK0A]",
                "3774 x 1063PC0  [17338-EM90A]",
                "3834 x 1063PC0  [17338-4CM0A]",
                "4232 x 2047PED  [46285-EM90A]",
                "4233 x 2047PED  [46285-EM92A]",
                "4292 x 2047PE0  [46285-4CM0A]",
                "4723 x 2047PE0  [46284-5BF0A]"
        };
        for (String partName : partNameArray) {
            partList.add(new Part(getNewId(), partName, true));
        }
    }

    private int getNewId() {
        return idCounter.getAndIncrement();
    }

    @Override
    public Part get(int id) {
        for (Part part : partList) {
            if (id == part.getId()) {
                return part;
            }
        }
        return null;
    }

    @Override
    public boolean save(Part part) {
        return false;
    }

    @Override
    public List<Part> getAll() {
        return partList;
    }

    @Override
    public void delete(int id) {

    }
}
