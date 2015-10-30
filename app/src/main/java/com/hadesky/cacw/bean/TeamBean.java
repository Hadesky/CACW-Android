package com.hadesky.cacw.bean;

/**
 * Created by 45517 on 2015/10/31.
 */
public class TeamBean {
    private String team_name;
    private long team_id;

    public TeamBean(String team_name, long team_id) {
        this.team_name = team_name;
        this.team_id = team_id;
    }

    public String getTeamName() {
        return team_name;
    }

    public void setTeamName(String team_name) {
        this.team_name = team_name;
    }

    public long getTeamId() {
        return team_id;
    }

    public void setTeamId(long team_id) {
        this.team_id = team_id;
    }
}
