package com._3line.gravity.freedom.dispute.dtos;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import com._3line.gravity.core.verification.utility.PrettySerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

@Data
public class DisputeDto  extends AbstractVerifiableDto implements PrettySerializer {

    private String agentName ;

    private Long tranId ;

    private String type ;

    private String comment ;

    private String amount ;

    private String action ;

    private String walletNumber ;

    private String raisedBy ;

    private String approvedBy ;



    @Override
    public  JsonSerializer<DisputeDto> getSerializer() {
        return new JsonSerializer<DisputeDto>() {
            @Override
            public void serialize(DisputeDto value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeStartObject();
                gen.writeStringField("AGENT ",value.getAgentName());
                if(Objects.nonNull(value.getTranId()) && StringUtils.isNotBlank(value.getTranId().toString())){
                    gen.writeStringField("TRAN ID ",value.getTranId().toString());
                }

                gen.writeStringField("TYPE",value.getType());
                gen.writeStringField("AMOUNT",value.getAmount());
                gen.writeStringField("WALLET",value.getWalletNumber());
                gen.writeStringField("REMARK",value.getComment());
                gen.writeStringField("ACTION",value.getAction());
                gen.writeStringField("RAISED BY",value.getRaisedBy());
                //TODO
                //TO be completed !!
                //TO be completed !!
                gen.writeEndObject();
            }
        };
    }
}
