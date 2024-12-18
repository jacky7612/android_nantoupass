package com.jotangi.nantouparking.JackyVariant

object json_sample {

    val checkChargeStatusSuccess        ="{\"status\":\"true\",\"code\":\"0x0200\",\"responseMessage\":\"success\"}"
    val checkChargeStatusInCharging     ="{\"status\":\"false\",\"code\":\"0x0201\",\"responseMessage\":\"目前正在充電中\"}"
    val checkChargeStatusHadUnpaidBill  ="{\"status\":\"false\",\"code\":\"0x0202\",\"responseMessage\":\"有未繳費充電帳單\"}"

    var checkCharge =checkChargeStatusSuccess

    val ChargeHistory4User =
        "{\n" +
                "    \"status\":\"true\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "        \"charge_history\": [\n" +
                "            {\n" +
                "            \"end_time\": \"2024/08/30 23:42:30\",\n" +
                "            \"fee\": \"0.0\",\n" +
                "            \"kwh\": \"0.0\",\n" +
                "            \"price\": \"10.0\",\n" +
                "            \"start_time\": \"2024/08/30 23:31:10\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"charge_point_id\": \"66a896ee69759e53d3ab0cc0\",\n" +
                "        \"charge_time\": \"11分\",\n" +
                "        \"charger_name\": \"充電車專用車位快充-T\",\n" +
                "        \"gun_name\": \"槍位 2\",\n" +
                "        \"header_type\": \"CCS2\",\n" +
                "        \"kwh\": \"5.21\",\n" +
                "        \"month\": \"2024-08\",\n" +
                "        \"order_id\": \"66d1e5be262710aa9e2dca01\",\n" +
                "        \"order_serial_number\": \"ML_0001_20240830_02\",\n" +
                "        \"pay_status\": \"付款成功，商店尚未請款\",\n" +
                "        \"price\": \"0\",\n" +
                "        \"start_soc\": \"–-\",\n" +
                "        \"start_time\": \"2024/08/30 23:31:10\",\n" +
                "        \"station_id\": \"66a87b0969759e53d3ab0cb9\",\n" +
                "        \"station_name\": \"縣民廣場平面停車場\",\n" +
                "        \"stop_reason\": \"APP結束交易(APP 使用者)\",\n" +
                "        \"stop_soc\": \"–-\",\n" +
                "        \"stop_time\": \"2024/08/30 23:42:30\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"status_code\": \"0x0200\",\n" +
                "    \"status_message\": \"succeed\"\n" +
                "}"

    val transQrcode ="{\"status\":\"true\"," +
                     "      \"data\":{\"ID\":\"66a88d7069759e53d3ab0cbd\"," +
                     "      \"address\":\"310新竹縣竹東鎮信義路189號\",\"charger_name\":\"充電車專用車位1\",\"header_type\":\"type1\",\"header_type_name\":\"Type\n" +
            "1(J1772)\",\"kw\":\"5.5\",\"order_id\":\"\",\"port\":1,\"pre_control\":false,\"pre_control_user\":false,\"price\":\"8\",\"station_name\":\"竹東信義立體停車場\",\"status\":\"可使用\",\"type\":\"AC\"},\"status_code\":\"0x0200\",\"status_message\":\"succeed\"}"

    var setPower ="{\n" +
            "\"status\":\"true\",\n" +
            "\"status_code\": \"0x0200\",\n" +
            "\"status_message\": \"success\",\n" +
            "\"data\": { \"ID\": \"訂單編號\", \"code\": 200 }\n" +
            "}"
}