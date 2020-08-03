package com.fh.mamber.service;

import com.fh.common.ServerResponse;
import com.fh.mamber.model.Mamber;

public interface MamberService {

    ServerResponse userNameList(String userName);

    ServerResponse phoneNumberList(String phoneNumber);

    ServerResponse addMamberList(Mamber mamber);

    ServerResponse login(Mamber mamber);
}
