package com.ev.srv.demoarq.model;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;


/**
 * ClientDirection
 */

@Data
@Builder
public class ClientDirection  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private String id;
  
  @JsonProperty("dir")
  private String dir;
  
  @JsonProperty("number")
  private String number;
  
}

