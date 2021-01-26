package com._3line.gravity.freedom._3linecms.service;


import com._3line.gravity.freedom._3linecms.dtos.CMSRequest;
import com._3line.gravity.freedom._3linecms.dtos.CMSResponse;

public interface CMSService {

    CMSResponse uploadCardDetails(CMSRequest cmsRequest);

    CMSResponse fundCard(CreditRequest creditRequest);
}
