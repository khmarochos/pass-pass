package ua.tucha.passpass.web.router;

// TODO: it would be cool to get rid of it
public class RouteRegistry {

    public static final String HOME = "/note/list"; // FIXME: a temporary measure

    public static class NoteRouteRegistry {
        public static final String FIRST_LEVEL = "/note";
        public static final String LIST = FIRST_LEVEL + "/list";
        public static final String CREATE = FIRST_LEVEL + "/create";
        public static final String READ = FIRST_LEVEL + "/read";
        public static final String UPDATE = FIRST_LEVEL + "/update";
        public static final String DELETE = FIRST_LEVEL + "/delete";
    }

    public static class UserRouteRegistry {
        public static final String FIRST_LEVEL = "/user";
        public static final String CREATE = FIRST_LEVEL + "/create"; // not the same as sign-up
        public static final String READ = FIRST_LEVEL + "/read";
        public static final String UPDATE = FIRST_LEVEL + "/update";
        public static final String DELETE = FIRST_LEVEL + "/delete";
        public static final String SIGN_OUT = FIRST_LEVEL + "/sign-out";
        public static final String SIGN_IN = FIRST_LEVEL + "/sign-in";
        public static final String RESET_PASSWORD_ORDER_TOKEN = FIRST_LEVEL + "/reset-password/order-token";
        public static final String RESET_PASSWORD_APPLY_TOKEN = FIRST_LEVEL + "/reset-password/apply-token";
        public static final String SIGN_UP = FIRST_LEVEL + "/sign-up";
        public static final String CONFIRM_EMAIL_ORDER_TOKEN = FIRST_LEVEL + "/confirm-email/order-token";
        public static final String CONFIRM_EMAIL_APPLY_TOKEN = FIRST_LEVEL + "/confirm-email/apply-token";
    }

    public static class FailureRouteRegistry {
        public static final String FIRST_LEVEL = "/failure";
        public static final String NOT_FOUND = FIRST_LEVEL + "/404";
        public static final String INTERNAL_SERVER_ERROR = FIRST_LEVEL + "/500";
        public static final String DEFAULT = FIRST_LEVEL + "/default";
    }

}