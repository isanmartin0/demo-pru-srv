package com.ev.srv.demoarq.model;

import lombok.Builder;
import lombok.Data;
import com.ev.srv.demoarq.model.ClientDirection;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;


/**
 * User
 */

@Data
@Builder
public class User  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private String id;
  
  @JsonProperty("entidad")
  private String entidad;
  
  @JsonProperty("dir")
  private ClientDirection dir;
  
}

