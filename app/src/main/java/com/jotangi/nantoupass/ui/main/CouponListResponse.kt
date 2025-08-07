

data class CouponListResponse(
    val cid: String,
    val coupon_id: String,
    val member_id: String,
    val coupon_name: String,
    val using_flag: String,
    val using_time: String?,
    val coupon_body: String,
    val coupon_description: String,
    val coupon_startdate: String,
    val coupon_enddate: String,
    val coupon_status: String,
    val coupon_rule: String,
    val discount_amount: String,
    val coupon_storeid: String?,
    val coupon_picture: String?,
    val coupon_created_at: String,
    val coupon_updated_at: String?,
    val coupon_trash: String
)
