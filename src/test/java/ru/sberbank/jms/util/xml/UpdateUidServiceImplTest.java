//package ru.sberbank.jms.util.xml;
//
//import org.junit.Test;
//
///**
// * Created with IntelliJ IDEA.
// * User: SBT-Yanushevsky-SA
// * Date: 21.11.13
// * Time: 10:43
// * To change this template use File | Settings | File Templates.
// */
//public class UpdateUidServiceImplTest {
//
//    public static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
//            "\n" +
//            "<IFXRqType xmlns:ns2=\"http://sbrf.ru/baseproduct/erib/adapter/1\">\n" +
//            "\n" +
//            "    <ns2:BankAcctInqRq>\n" +
//            "\n" +
//            "        <ns2:RqUID>4d764cabf4d9e2d6b8dcca4afaea1da4</ns2:RqUID>\n" +
//            "\n" +
//            "        <ns2:RqTm>2013-06-04T18:12:07</ns2:RqTm>\n" +
//            "\n" +
//            "        <ns2:OperUID>AA65499dae7add1aa5b7ed5828e49485</ns2:OperUID>\n" +
//            "\n" +
//            "        <ns2:SPName>BP_ERIB</ns2:SPName>\n" +
//            "\n" +
//            "        <ns2:BankInfo>\n" +
//            "\n" +
//            "            <ns2:RbTbBrchId>38000000</ns2:RbTbBrchId>\n" +
//            "\n" +
//            "        </ns2:BankInfo>\n" +
//            "\n" +
//            "        <ns2:CustInfo>\n" +
//            "\n" +
//            "            <ns2:PersonInfo>\n" +
//            "\n" +
//            "                <ns2:Birthday>1972-09-30</ns2:Birthday>\n" +
//            "\n" +
//            "                <ns2:PersonName>\n" +
//            "\n" +
//            "                    <ns2:LastName>Удобнов</ns2:LastName>\n" +
//            "\n" +
//            "                    <ns2:FirstName>Ериб</ns2:FirstName>\n" +
//            "\n" +
//            "                    <ns2:MiddleName>Автотестович</ns2:MiddleName>\n" +
//            "\n" +
//            "                </ns2:PersonName>\n" +
//            "\n" +
//            "                <ns2:IdentityCard>\n" +
//            "\n" +
//            "                    <ns2:IdType>21</ns2:IdType>\n" +
//            "\n" +
//            "                    <ns2:IdSeries>46 11</ns2:IdSeries>\n" +
//            "\n" +
//            "                    <ns2:IdNum>913711</ns2:IdNum>\n" +
//            "\n" +
//            "                    <ns2:IssuedBy>Отделом УФМС России по Ульяновской области в Засвияжском р-не г. Ульяновска ,730</ns2:IssuedBy>\n" +
//            "\n" +
//            "                    <ns2:IssuedCode>23242414</ns2:IssuedCode>\n" +
//            "\n" +
//            "                    <ns2:IssueDt>2010-11-11</ns2:IssueDt>\n" +
//            "\n" +
//            "                    <ns2:ExpDt>2015-11-11</ns2:ExpDt>\n" +
//            "\n" +
//            "                </ns2:IdentityCard>\n" +
//            "\n" +
//            "            </ns2:PersonInfo>\n" +
//            "\n" +
//            "        </ns2:CustInfo>\n" +
//            "\n" +
//            "        <ns2:AcctType>Deposit</ns2:AcctType>\n" +
//            "\n" +
//            "        <ns2:AcctType>Card</ns2:AcctType>\n" +
//            "\n" +
//            "        <ns2:AcctType>Credit</ns2:AcctType>\n" +
//            "\n" +
//            "        <ns2:AcctType>IMA</ns2:AcctType>\n" +
//            "\n" +
//            "        <ns2:AcctType>DepoAcc</ns2:AcctType>\n" +
//            "\n" +
//            "    </ns2:BankAcctInqRq>\n" +
//            "\n" +
//            "</IFXRqType>";
//
//    @Test
//    public void testUpdateUid() throws Exception {
//
//        String xml = new UpdateUidServiceImpl().updateUid(XML);
//        System.out.println(xml);
//    }
//}
