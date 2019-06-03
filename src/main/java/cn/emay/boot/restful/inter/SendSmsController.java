package cn.emay.boot.restful.inter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.common.FileStoreCommon;
import cn.emay.boot.common.RedisKeys;
import cn.emay.boot.dto.inter.GetSmsResult;
import cn.emay.boot.dto.inter.SendSmsResult;
import cn.emay.boot.dto.inter.SmsMessage;
import cn.emay.boot.utils.FileStoreUtils;
import cn.emay.json.JsonHelper;
import cn.emay.redis.RedisClient;
import cn.emay.store.file.queue.FileQueue;

/**
 * 对外接口
 * 
 * @author Frank
 *
 */
@RestController
@RequestMapping("/sms")
public class SendSmsController {

	private Logger log = LoggerFactory.getLogger(SendSmsController.class);

	@Autowired
	private RedisClient redis;

	/**
	 * 接收短信，进行规则校验，然后放入文件队列中
	 * 
	 * @param appId
	 * @param message
	 * @param mobile
	 * @return
	 */
	@GetMapping(value = "/send")
	public SendSmsResult sendSms(String appId, String message, String mobile) {
		try { 
			if (appId == null) {
				return SendSmsResult.errorResult("appId is null");
			} 
			if (message == null) {
				return SendSmsResult.errorResult("message is null");
			}
			if (mobile == null) {
				return SendSmsResult.errorResult("mobile is null");
			}
			SmsMessage sms = new SmsMessage(appId, message, mobile);
			FileQueue queue = FileStoreUtils.getQueue(FileStoreCommon.CACHE_SMS_QUEUE);
			queue.offer(JsonHelper.toJsonString(sms));
			log.info("sendsimpleSms success : " + sms.toString());
			return SendSmsResult.successResult(sms.getSmsId());
		} catch (Exception e) {
			log.error("sendsimpleSms error", e);
			return SendSmsResult.errorResult("system error");
		}
	}

	/**
	 * 获取队列中所有的短信
	 * 
	 * @param appId
	 * @return
	 */
	@GetMapping(value = "/get/{appId}")
	public GetSmsResult getSms(@PathVariable("appId") String appId) {
		try {
			if (appId == null) {
				return GetSmsResult.errorResult("appId is null");
			}
			List<SmsMessage> smses = new ArrayList<>();
			SmsMessage sms = null;
			while ((sms = redis.rpop(RedisKeys.SMS_LIST_KEY + appId, SmsMessage.class)) != null) {
				smses.add(sms);
			}
			log.info("sendsimpleSms success : " + smses.size());
			return GetSmsResult.successResult(smses);
		} catch (Exception e) {
			log.error("sendsimpleSms error", e);
			return GetSmsResult.errorResult("system error");
		}
	}

}