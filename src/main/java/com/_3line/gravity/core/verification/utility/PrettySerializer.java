package com._3line.gravity.core.verification.utility;

import com.fasterxml.jackson.databind.JsonSerializer;

public interface PrettySerializer
{
    public  <T> JsonSerializer<T> getSerializer();
}
