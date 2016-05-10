package com.ktung.watchit.api.trakt.entities;

import org.joda.time.DateTime;

import java.util.List;

public class ShowResponse {
    // Sync Watched & Progress watched
    public DateTime last_watched_at;
    public List<SeasonResponse> seasons;

    // Sync watched & most watched
    public Show show;

    // Most watched (/shows/watched/)
    public Integer watcher_count;
    public Integer play_count;
    public Integer collected_count;
    public Integer collector_count;

    // Sync Watched (/sync/watched/shows)
    public Integer plays;

    // Progress watched (/shows/id/progress/watched)
    public Integer aired;
    public Integer completed;
    public List<Season> hidden_seasons;
    public Episode next_episode;
}
