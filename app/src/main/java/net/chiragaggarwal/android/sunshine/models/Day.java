package net.chiragaggarwal.android.sunshine.models;

public enum Day {
    SUNDAY {
        public Day next() {
            return Day.MONDAY;
        }

        public Day previous() {
            return Day.SATURDAY;
        }
    }, MONDAY {
        public Day next() {
            return Day.TUESDAY;
        }

        public Day previous() {
            return Day.SUNDAY;
        }
    }, TUESDAY {
        public Day next() {
            return Day.WEDNESDAY;
        }

        public Day previous() {
            return Day.MONDAY;
        }
    }, WEDNESDAY {
        public Day next() {
            return Day.THURSDAY;
        }

        public Day previous() {
            return Day.TUESDAY;
        }
    }, THURSDAY {
        public Day next() {
            return Day.FRIDAY;
        }

        public Day previous() {
            return Day.WEDNESDAY;
        }
    }, FRIDAY {
        public Day next() {
            return Day.SATURDAY;
        }

        public Day previous() {
            return Day.THURSDAY;
        }
    }, SATURDAY {
        public Day next() {
            return Day.SUNDAY;
        }

        public Day previous() {
            return Day.FRIDAY;
        }
    };

    public Day next() {
        return this.next();
    }

    public Day previous() {
        return this.previous();
    }

    public static Day parse(String dayString) {
        Day day = null;
        switch (dayString) {
            case "Monday":
                day = Day.MONDAY;
                break;
            case "Tuesday":
                day = Day.TUESDAY;
                break;
            case "Wednesday":
                day = Day.WEDNESDAY;
                break;
            case "Thursday":
                day = Day.THURSDAY;
                break;
            case "Friday":
                day = Day.FRIDAY;
                break;
            case "Saturday":
                day = Day.SATURDAY;
                break;
            case "Sunday":
                day = Day.SUNDAY;
                break;
        }
        return day;
    }
}
