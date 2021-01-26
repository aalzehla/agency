package com._3line.gravity.freedom.billpayment.service.implementation;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.usermgt.exception.AppUserServiceException;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.billpayment.dto.BillPaymentViewDto;
import com._3line.gravity.freedom.billpayment.dtos.*;
import com._3line.gravity.freedom.billpayment.dto.BillCategoryViewDto;
import com._3line.gravity.freedom.billpayment.dto.BillOptionViewDto;
import com._3line.gravity.freedom.billpayment.dto.BillServicesViewDto;
import com._3line.gravity.freedom.billpayment.models.BillCategory;
import com._3line.gravity.freedom.billpayment.models.BillOption;
import com._3line.gravity.freedom.billpayment.models.BillPayment;
import com._3line.gravity.freedom.billpayment.models.BillServices;
import com._3line.gravity.freedom.billpayment.repository.BillCategoryRepo;
import com._3line.gravity.freedom.billpayment.repository.BillOptionsRepo;
import com._3line.gravity.freedom.billpayment.repository.BillPaymentRepo;
import com._3line.gravity.freedom.billpayment.repository.BillServicesRepo;
import com._3line.gravity.freedom.billpayment.service.Billservice;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.utils.MySSLSocketFactory;
import com._3line.gravity.freedom.financialInstitutions.magtipon.model.MagtiponLogs;
import com._3line.gravity.freedom.financialInstitutions.magtipon.repository.MagtiponLogsRepository;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.service.InstitutionService;
import com._3line.gravity.freedom.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;

/**
 * @author JoyU
 * @date 10/15/2018
 */
@Service
public class BillserviceImpl implements Billservice {

    @Autowired
    BillCategoryRepo billCategoryRepo;

    @Autowired
    BillServicesRepo billServicesRepo;

    @Autowired
    BillOptionsRepo billOptionsRepo;

    @Autowired
    BillPaymentRepo billPaymentRepo;

    @Qualifier("gravityDailyCommissionServiceImpl")
    @Autowired
    GravityDailyCommissionService gravityDailyCommissionService;

    @Value("${magtiponservices.username}")
    private String username;

    @Value("${magtiponservices.primarykey}")
    private String primaryKey;

    @Value("${magtiponservices.paybills}")
    private String paymentsUrl;

    @Value("${magtiponservices.validate}")
    private String customerUrl;

    @Value("${magtiponservices.renderservice}")
    private String servicesUrl;

    @Autowired
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper = new ObjectMapper();

    Gson gson = new Gson();
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private MessageSource messageSource;
    private Locale locale;

    @Autowired
    private MagtiponLogsRepository logsRepository;

    @Autowired
    InstitutionService institutionService;

    @Override
    public List<BillCategory> getAllCategories() {
    List<BillCategory> billCategories = billCategoryRepo.findAll();
        billCategories.forEach(billCategory -> {
            billCategory.getServices().forEach(billServices -> {
                billServices.setOptions(null);
            });
        });
    return billCategories;
    }

    @Override
    public BillPaymentDto convertEntityToDTO(Agents agents) {
        return null;
    }

    @Override
    public BillPaymentDto convertEntityToDTO(BillServicesDto agents) {

//        AgentDto dto = modelMapper.map(agents, BillServicesDto.class) ;
//        dto.setBuisnessLocationEntryDate(DateUtil.formatDateToreadable(agents.getPresentAddressDateOfEntry()));
//        dto.setDateOfBirth(DateUtil.formatDateToreadable(agents.getDob()));
////        try {
////            dto.setParentAgent(agents.getParentAgentId());
////
////        }catch (Exception e){
////
////        }
//        return modelMapper.map(agents, BillServicesDto.class);

        return null;
    }

    @Override
    public Page<BillServicesDto> getBillservices(Pageable pageDetails) {
//        logger.debug("Retrieving agents");
//        Page<BillServices> page = billServicesRepo.findAll(pageDetails);
//        List<BillServicesDto> dtOs = ;
//        long t = page.getTotalElements();
//        Page<BillServicesDto> pageImpl = new PageImpl<BillServicesDto>(dtOs, pageDetails, t);
//        return pageImpl;

        return null;
    }

    @Override
    public Collection<BillServices> getServicesByCategoryId(Long id) {
        BillCategory billCategory = billCategoryRepo.getOne(id);
        Collection<BillServices> billServices = billCategory.getServices();
        return billServices;
    }

    @Override
    public Collection<BillServices> getServicesForCategory(Long id) {
        Collection<BillServices> billServices = billServicesRepo.findByCategory(id);
        return billServices;
    }

    @Override
    public Collection<BillOption> getOptionsByServiceId(Long id) {
        BillServices billServices = billServicesRepo.getOne(id);
        Collection<BillOption> billOptions = billServices.getOptions();
        return billOptions;
    }

    @Override
    public String updateBillServices() throws Exception{
        Long systemtime = System.currentTimeMillis();
        String time = systemtime.toString().substring(0, 10);
        String encodedString = "";
        byte [] a  = Utility.sha512Byte(systemtime + primaryKey) ;
        String sign = Base64.getEncoder().encodeToString(a) ;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            String string = time + primaryKey;

            md.update(string.getBytes());

            byte[] digest = md.digest();

            encodedString = Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(Billservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set("Authorization", "magtipon " + username + ":" + encodedString);
        httpHeaders.set("timestamp", time);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> re = restTemplate.exchange(servicesUrl, HttpMethod.GET, entity, String.class);

        logger.info("magtipon response ------>{}", re.getBody());
        BillCategory billCategory = new BillCategory();
        BillCategoryDto billCategoryDto = objectMapper.readValue(re.getBody(), BillCategoryDto.class);
        modelMapper.map(billCategoryDto, billCategory);
        billCategoryRepo.save(billCategory);
        billCategoryDto.getServices().forEach(s -> {
            String serviceName = s.getServiceName();
            BillServices billServicesCheck = billServicesRepo.findByServiceName(serviceName);
            if (billServicesCheck == null) {
                BillServices billServices = new BillServices();
                modelMapper.map(s, billServices);
                billServices.setServiceCategory(billCategory);
                BillServices saved = billServicesRepo.save(billServices);
                s.getOptions().forEach(o -> {
                    String code = o.getCode();
                    BillOption billOptionCheck = billOptionsRepo.findByCode(code);
                    if (billOptionCheck == null) {
                        BillOption billOption = new BillOption();
                        modelMapper.map(o, billOption);
                        billOption.setServiceOption(saved);
                        billOptionsRepo.save(billOption);
                    }
                });
            }
        });

        logger.info("convert to {}", billCategoryDto.toString());
        if(billCategoryDto.getResponseCode().equals("90000")){
            return billCategoryDto.getResponseCode();
        }
        return "999";
    }

    @Override
    public BillPaymentDto payBills(BillPaymentDto billPaymentDto){
        BillPaymentDto billPaymentDtoResponse;
        Long systemtime = System.currentTimeMillis();
        String time = systemtime.toString().substring(0, 10);
//        String requestRef = String.valueOf(systemtime);
        String encodedString = "";
        byte [] a  = Utility.sha512Byte(billPaymentDto.getRequestRef() + primaryKey) ;
        String sign = Base64.getEncoder().encodeToString(a) ;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String string = time + primaryKey;
            md.update(string.getBytes());
            byte[] digest = md.digest();
            encodedString = Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(Billservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("encoded {}", sign);
        billPaymentDto.setSignature(sign);
        billPaymentDto.setRequestRef(billPaymentDto.getRequestRef());

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(ccm,params);

            ObjectMapper mapper  = new ObjectMapper() ;
            logger.info("body before json {}", billPaymentDto.toString());
            String body = mapper.writeValueAsString(billPaymentDto) ;
            System.out.println("The URL *** " +paymentsUrl);
            org.apache.http.HttpEntity httpEntity = new ByteArrayEntity(body.getBytes(StandardCharsets.UTF_8));
            HttpPost httpPost = new HttpPost(paymentsUrl);
            httpPost.addHeader("Authorization","magtipon " + username + ":" + encodedString);
            httpPost.addHeader("timestamp",time);
            httpPost.addHeader("Content-Type","application/json; charset=utf-8");
            httpPost.setEntity(httpEntity);
            logger.info("sending request .......");

            MagtiponLogs logs = new MagtiponLogs() ;
            logs.setService("BILL_PAYMENT");
            logs.setHeaders(httpPost.getAllHeaders().toString());
            logs.setRequest(body);
            MagtiponLogs saved = logsRepository.save(logs);

            HttpResponse response = httpClient.execute(httpPost);
            String res = EntityUtils.toString(response.getEntity()) ;

            logger.info("Received response is "+res);

            saved.setResponse(res);
            logsRepository.save(saved);

            billPaymentDtoResponse = gson.fromJson(res, BillPaymentDto.class);//objectMapper.readValue(res, BillPaymentDto.class);
            logger.info("Bill Payment response Code --- {}", billPaymentDtoResponse.getResponseCode());

        }catch (Exception e){
            e.printStackTrace();
            throw  new GravityException("Issuer or switch inoperative") ;
        }

        return billPaymentDtoResponse;
    }

    @Override
    public ValidateCustomerResponse validate(ValidateCustomerRequest request,Agents agent) {
        ValidateCustomerResponse validateCustomerResponse;
        Long systemtime = System.currentTimeMillis();
        String time = systemtime.toString().substring(0, 10);
        String requestRef = String.valueOf(systemtime);
        String encodedString = "";
        byte [] a  = Utility.sha512Byte(requestRef + primaryKey) ;
        String sign = Base64.getEncoder().encodeToString(a) ;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String string = time + primaryKey;
            md.update(string.getBytes());
            byte[] digest = md.digest();
            encodedString = Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(Billservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("encoded {}", sign);

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(ccm,params);

            ObjectMapper mapper  = new ObjectMapper() ;
            logger.info("body before json {}", request.toString());
            String body = mapper.writeValueAsString(request) ;
            System.out.println("The URL *** " +customerUrl);
            org.apache.http.HttpEntity httpEntity = new ByteArrayEntity(body.getBytes(StandardCharsets.UTF_8));
            HttpPost httpPost = new HttpPost(customerUrl);
            httpPost.addHeader("Authorization","magtipon " + username + ":" + encodedString);
            httpPost.addHeader("timestamp",time);
            httpPost.addHeader("Content-Type","application/json; charset=utf-8");
            httpPost.setEntity(httpEntity);
            logger.info("sending request .......");

            MagtiponLogs logs = new MagtiponLogs() ;
            logs.setService("BILL_CUSTOMER_ENQUIRY");
            logs.setHeaders(httpPost.getAllHeaders().toString());
            logs.setRequest(body);
            MagtiponLogs saved = logsRepository.save(logs);

            HttpResponse response = httpClient.execute(httpPost);
            String res = EntityUtils.toString(response.getEntity()) ;

            res.replace("₦","");

            logger.info("Received response is "+res);

            saved.setResponse(res);
            logsRepository.save(saved);

            Map <String , String>data = objectMapper.readValue(res, HashMap.class);
            validateCustomerResponse = new ValidateCustomerResponse();
            validateCustomerResponse.setCustomerName(((data.get("CustomerName"))));
            validateCustomerResponse.setResponseCode(((data.get("ResponseCode"))));
            validateCustomerResponse.setResponseDescription(((data.get("ResponseDescription"))));

            BillOption billOption = billOptionsRepo.findByCode(request.getPaymentCode());
            Map<String,String> amountWithChargeMap;

            if(billOption!=null){

                InstitutionDTO institutionDTO = institutionService.getInstitutionByAgentId(agent.getParentAgentId()) ;
                amountWithChargeMap = gravityDailyCommissionService.getBillAmountWithCharge(Double.parseDouble(request.getAmount()),billOption,institutionDTO);
                String amountWithCharge = amountWithChargeMap.get("totalAmount");
                String charge = amountWithChargeMap.get("charge");

                validateCustomerResponse.setTotalamount(amountWithCharge);
                validateCustomerResponse.setCharge(charge);
            }



            logger.info("Bill Payment  Customer response Code --- {}", validateCustomerResponse);

        }catch (Exception e){
            e.printStackTrace();
            throw  new GravityException("Issuer or switch inoperative") ;
        }

        return validateCustomerResponse;
    }


    @Override
    public void updateCategory(Long serviceId, Long category) {
        BillServices billerService = billServicesRepo.getOne(serviceId);
        billerService.setCategory(category);
        billServicesRepo.save(billerService);
    }

    @Override
    public Page<BillServicesViewDto> findAllBillServiceDTOPageable(Pageable pageable) {

        Page<BillServices> page = billServicesRepo.findAllBy(pageable);
        List<BillServicesViewDto> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<BillServicesViewDto> pageImpl = new PageImpl<BillServicesViewDto>(dtOs, pageable, t);
        return pageImpl;
    }

    @Override
    public BillServicesViewDto getBillById(Long id) {
        logger.debug("Retrieving Bill Details for [{}]", id);
        BillServices billServices = billServicesRepo.findBillById(id);
        return  convertEntityToDto(billServices);
    }

    @Override
    public String updateService(BillServicesViewDto billServicesViewDto) {
        logger.debug("Updating bill details:  {}", billServicesViewDto);

        return updateBill(billServicesViewDto);
    }

    @Override
    public Page<BillPaymentViewDto> findAllBillPaymentDTOPageable(Pageable pageable) {

        Page<BillPayment> page = billPaymentRepo.findAllByOrderByIdDesc(pageable);
        List<BillPaymentViewDto> dtos = convertEntitiesToDTO(page.getContent());
        long t = page.getTotalElements();
        Page<BillPaymentViewDto> pageImpl = new PageImpl<BillPaymentViewDto>(dtos, pageable, t);
        return pageImpl;
    }

    private String updateBill(BillServicesViewDto billServicesViewDto){

        try {
            BillServices billServices = billServicesRepo.findBillById(billServicesViewDto.getId());

            billServices.setCategory(billServicesViewDto.getCategory());


            BillServices updatedBill= billServicesRepo.save(billServices);
            logger.info("Updated Bill Service [{}]", updatedBill);
            return messageSource.getMessage("bill.update.success", null, locale);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("bill.update.failure", null, locale), e);
        }
    }

    public BillServicesViewDto convertEntityToDto(BillServices billServices){
        BillServicesViewDto billServicesDto = modelMapper.map(billServices, BillServicesViewDto.class);

        return billServicesDto;
    }

    public List<BillServicesViewDto> convertEntitiesToDTOs(List<BillServices> billServices){
        List<BillServicesViewDto> billServicesDtos = new ArrayList<>();
        for(BillServices bill: billServices){
            BillServicesViewDto billServicesDto = convertEntityToDto(bill);
            billServicesDtos.add(billServicesDto);
        }
        return billServicesDtos;
    }

    public List<BillPaymentViewDto> convertEntitiesToDTO(List<BillPayment> billPayments){
        List<BillPaymentViewDto> billPaymentViewDtos = new ArrayList<>();
        for(BillPayment bill: billPayments){
            BillPaymentViewDto billPaymentViewDto = convertEntityToDtos(bill);
            billPaymentViewDtos.add(billPaymentViewDto);
        }
        return billPaymentViewDtos;
    }

    public BillPaymentViewDto convertEntityToDtos(BillPayment billPayment){
        BillPaymentViewDto billPaymentViewDto = modelMapper.map(billPayment, BillPaymentViewDto.class);
        billPaymentViewDto.setOptionName(billOptionsRepo.findByCode(billPayment.getPaymentCode()).getOptionName());
        return billPaymentViewDto;
    }


}
