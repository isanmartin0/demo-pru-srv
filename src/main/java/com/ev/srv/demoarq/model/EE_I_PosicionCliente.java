package com.ev.srv.demoarq.model;

import com.ev.srv.demoarq.model.User.UserBuilder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EE_I_PosicionCliente {
	private String codigoEntidad;
	private String idInternoPe;
	private String tipoAcuerdo;
	private String codigoEstado;
	private String relacionAcuerdoPersona;
	private String ecvPersonaAcuerdo;
	
}
