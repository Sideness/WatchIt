package com.ktung.watchit.api.trakt.entities;

public class EpisodeCheckinRequest extends BaseCheckinRequest {
    public Episode episode;

    public static class Builder {
        private SharingSettings sharing;
        private String message;
        private String venue_id;
        private String venue_name;
        private String app_version;
        private String app_date;
        private Episode episode;

        public Builder(Episode episode) {
            if (null == episode) {
                throw new IllegalArgumentException("Episode must not be null");
            }

            this.episode = episode;
        }

        public Builder sharing(SharingSettings sharing) {
            this.sharing = sharing;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder venueId(String venue_id) {
            this.venue_id = venue_id;
            return this;
        }

        public Builder venueName(String venue_name) {
            this.venue_name = venue_name;
            return this;
        }

        public Builder appVersion(String app_version) {
            this.app_version = app_version;
            return this;
        }

        public Builder appDate(String app_date) {
            this.app_date = app_date;
            return this;
        }

        public EpisodeCheckinRequest build() {
            EpisodeCheckinRequest rq = new EpisodeCheckinRequest();
            rq.episode = episode;
            rq.sharing = sharing;
            rq.message = message;
            rq.venue_id = venue_id;
            rq.venue_name = venue_name;
            rq.app_version = app_version;
            rq.app_date = app_date;

            return rq;
        }
    }
}
