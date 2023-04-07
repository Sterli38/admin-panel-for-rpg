package com.example.demo.dao.inMemory;

import com.example.demo.entity.Player;
import com.example.demo.filter.Filter;
import com.example.demo.filter.PlayerOrder;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {
    private final Filter filter;

    public PlayerComparator (Filter filter) {
        this.filter = filter;
    }

    @Override
    public int compare(Player o1, Player o2) {
        if(filter.getOrder() == PlayerOrder.ID) {
            return o1.getId().compareTo(o2.getId());
        } else if (filter.getOrder() == PlayerOrder.EXPERIENCE) {
            return o1.getExperience().compareTo(o2.getExperience());
        } else if (filter.getOrder() == PlayerOrder.NAME) {
            return o1.getName().compareTo(o2.getName());
        } else if  (filter.getOrder() == PlayerOrder.BIRTHDAY) {
            return o1.getBirthday().compareTo(o2.getBirthday());
        } else if (filter.getOrder() == PlayerOrder.LEVEL) {
            return o1.getLevel().compareTo(o2.getLevel());
        }
        return 0;
    }
}
