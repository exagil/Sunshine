package net.chiragaggarwal.android.sunshine.models;

public enum Day {
    SUNDAY {
        public Day next() {
            return Day.MONDAY;
        }
    }, MONDAY {
        public Day next() {
            return Day.TUESDAY;
        }
    }, TUESDAY {
        public Day next() {
            return Day.WEDNESDAY;
        }
    }, WEDNESDAY {
        public Day next() {
            return Day.THURSDAY;
        }
    }, THURSDAY {
        public Day next() {
            return Day.FRIDAY;
        }
    }, FRIDAY {
        public Day next() {
            return Day.SATURDAY;
        }
    }, SATURDAY {
        public Day next() {
            return Day.SUNDAY;
        }
    };

    public Day next() {
        return this.next();
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
