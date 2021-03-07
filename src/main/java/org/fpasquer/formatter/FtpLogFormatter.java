package org.fpasquer.formatter;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FtpLogFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        LocalDate now = LocalDate.now();
        return MessageFormat.format("[{0}] : {1} -> {2}{3}",
                now.format(DateTimeFormatter.ISO_DATE),
                record.getLevel().toString(),
                record.getMessage(),
                System.lineSeparator()
        );
    }
}
