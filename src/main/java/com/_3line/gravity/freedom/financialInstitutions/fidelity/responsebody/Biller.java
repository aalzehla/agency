package com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody;

public class Biller {

    private String Type;
    private int Id;
    private int PayDirectProductId;
    private int PayDirectInstitutionId;
    private String Name;
    private String ShortName;
    private String Narration;
    private String CustomerField1;
    private String LogoUrl;
    private String Surcharge;
    private String CurrencyCode;
    private String CurrencySymbol;
    private String QuickTellerSiteUrlName;
    private String SupportEmail;
    private String RiskCategoryId;
    private String CategoryId;
    private String CategoryName;
    private String MediumImageId;
    private String AmountType;
    private String CustomerField2;
    private String Url;
    private String CustomSectionUrl;
    private String Image;
    private String LargeImageId;
    private String PaymentOptionsPageHeader;
    private String PaymentOptionsTitle;
    private String NetworkId;
    private String ProductCode;
    private String SmallImageId;

    private PageFlowInfo PageFlowInfo;

    public PageFlowInfo getPageFlowInfo() {
        return PageFlowInfo;
    }

    public void setPageFlowInfo(PageFlowInfo pageFlowInfo) {
        PageFlowInfo = pageFlowInfo;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPayDirectProductId() {
        return PayDirectProductId;
    }

    public void setPayDirectProductId(int payDirectProductId) {
        PayDirectProductId = payDirectProductId;
    }

    public int getPayDirectInstitutionId() {
        return PayDirectInstitutionId;
    }

    public void setPayDirectInstitutionId(int payDirectInstitutionId) {
        PayDirectInstitutionId = payDirectInstitutionId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String narration) {
        Narration = narration;
    }

    public String getCustomerField1() {
        return CustomerField1;
    }

    public void setCustomerField1(String customerField1) {
        CustomerField1 = customerField1;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    public String getSurcharge() {
        return Surcharge;
    }

    public void setSurcharge(String surcharge) {
        Surcharge = surcharge;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return CurrencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        CurrencySymbol = currencySymbol;
    }

    public String getQuickTellerSiteUrlName() {
        return QuickTellerSiteUrlName;
    }

    public void setQuickTellerSiteUrlName(String quickTellerSiteUrlName) {
        QuickTellerSiteUrlName = quickTellerSiteUrlName;
    }

    public String getSupportEmail() {
        return SupportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        SupportEmail = supportEmail;
    }

    public String getRiskCategoryId() {
        return RiskCategoryId;
    }

    public void setRiskCategoryId(String riskCategoryId) {
        RiskCategoryId = riskCategoryId;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getMediumImageId() {
        return MediumImageId;
    }

    public void setMediumImageId(String mediumImageId) {
        MediumImageId = mediumImageId;
    }

    public String getAmountType() {
        return AmountType;
    }

    public void setAmountType(String amountType) {
        AmountType = amountType;
    }

    public String getCustomerField2() {
        return CustomerField2;
    }

    public void setCustomerField2(String customerField2) {
        CustomerField2 = customerField2;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getCustomSectionUrl() {
        return CustomSectionUrl;
    }

    public void setCustomSectionUrl(String customSectionUrl) {
        CustomSectionUrl = customSectionUrl;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLargeImageId() {
        return LargeImageId;
    }

    public void setLargeImageId(String largeImageId) {
        LargeImageId = largeImageId;
    }

    public String getPaymentOptionsPageHeader() {
        return PaymentOptionsPageHeader;
    }

    public void setPaymentOptionsPageHeader(String paymentOptionsPageHeader) {
        PaymentOptionsPageHeader = paymentOptionsPageHeader;
    }

    public String getPaymentOptionsTitle() {
        return PaymentOptionsTitle;
    }

    public void setPaymentOptionsTitle(String paymentOptionsTitle) {
        PaymentOptionsTitle = paymentOptionsTitle;
    }

    public String getNetworkId() {
        return NetworkId;
    }

    public void setNetworkId(String networkId) {
        NetworkId = networkId;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getSmallImageId() {
        return SmallImageId;
    }

    public void setSmallImageId(String smallImageId) {
        SmallImageId = smallImageId;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Biller{");
        sb.append("Type='").append(Type).append('\'');
        sb.append(", Id=").append(Id);
        sb.append(", PayDirectProductId=").append(PayDirectProductId);
        sb.append(", PayDirectInstitutionId=").append(PayDirectInstitutionId);
        sb.append(", Name='").append(Name).append('\'');
        sb.append(", ShortName='").append(ShortName).append('\'');
        sb.append(", Narration='").append(Narration).append('\'');
        sb.append(", CustomerField1='").append(CustomerField1).append('\'');
        sb.append(", LogoUrl='").append(LogoUrl).append('\'');
        sb.append(", Surcharge='").append(Surcharge).append('\'');
        sb.append(", CurrencyCode='").append(CurrencyCode).append('\'');
        sb.append(", CurrencySymbol='").append(CurrencySymbol).append('\'');
        sb.append(", QuickTellerSiteUrlName='").append(QuickTellerSiteUrlName).append('\'');
        sb.append(", SupportEmail='").append(SupportEmail).append('\'');
        sb.append(", RiskCategoryId='").append(RiskCategoryId).append('\'');
        sb.append(", PageFlowInfo='").append(PageFlowInfo).append('\'');
        sb.append(", CategoryId='").append(CategoryId).append('\'');
        sb.append(", CategoryName='").append(CategoryName).append('\'');
        sb.append(", MediumImageId='").append(MediumImageId).append('\'');
        sb.append(", AmountType='").append(AmountType).append('\'');
        sb.append(", CustomerField2='").append(CustomerField2).append('\'');
        sb.append(", Url='").append(Url).append('\'');
        sb.append(", CustomSectionUrl='").append(CustomSectionUrl).append('\'');
        sb.append(", Image='").append(Image).append('\'');
        sb.append(", LargeImageId='").append(LargeImageId).append('\'');
        sb.append(", PaymentOptionsPageHeader='").append(PaymentOptionsPageHeader).append('\'');
        sb.append(", PaymentOptionsTitle='").append(PaymentOptionsTitle).append('\'');
        sb.append(", NetworkId='").append(NetworkId).append('\'');
        sb.append(", ProductCode='").append(ProductCode).append('\'');
        sb.append(", SmallImageId='").append(SmallImageId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
