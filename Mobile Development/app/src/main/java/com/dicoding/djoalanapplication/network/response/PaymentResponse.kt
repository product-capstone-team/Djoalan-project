package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

	@field:SerializedName("resp")
	val resp: Resp? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Resp(

	@field:SerializedName("reference_id")
	val referenceId: String? = null,

	@field:SerializedName("actions")
	val actions: Actions? = null,

)

data class Actions(

	@field:SerializedName("mobile_deeplink_checkout_url")
	val mobileDeeplinkCheckoutUrl: Any? = null,

	@field:SerializedName("mobile_web_checkout_url")
	val mobileWebCheckoutUrl: String? = null,

	@field:SerializedName("qr_checkout_string")
	val qrCheckoutString: Any? = null,

	@field:SerializedName("desktop_web_checkout_url")
	val desktopWebCheckoutUrl: String? = null
)
