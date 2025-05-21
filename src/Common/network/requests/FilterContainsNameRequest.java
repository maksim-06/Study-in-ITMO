package Common.network.requests;

import Common.utiluty.Commands;

public class FilterContainsNameRequest extends Request {
    public final String name;

    public FilterContainsNameRequest (String name) {
        super(Commands.FILTER_CONTAINS_PART_NUMBER);
        this.name = name;
    }}
