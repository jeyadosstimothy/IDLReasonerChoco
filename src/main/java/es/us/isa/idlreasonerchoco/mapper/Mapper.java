package es.us.isa.idlreasonerchoco.mapper;

import es.us.isa.idlreasonerchoco.configuration.IDLConfiguration;
import es.us.isa.idlreasonerchoco.configuration.IDLException;

public abstract class Mapper {
	
    protected final IDLConfiguration configuration;

    protected Mapper(IDLConfiguration configuration) throws IDLException {
        this.configuration = configuration;
    }

}
