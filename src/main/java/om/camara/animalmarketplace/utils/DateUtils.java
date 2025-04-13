package om.camara.animalmarketplace.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component("dates")
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public String format(Date date, String pattern) {
        if (date == null || pattern == null) {
            return "";
        }
        logger.info("Formatting Date: {} with pattern: {}", date, pattern);
        return new SimpleDateFormat(pattern).format(date);
    }

    public String format(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null || pattern == null) {
            return "";
        }
        logger.info("Formatting LocalDateTime: {} with pattern: {}", localDateTime, pattern);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return format(date, pattern);
    }
}