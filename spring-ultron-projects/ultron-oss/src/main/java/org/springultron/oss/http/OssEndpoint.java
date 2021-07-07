package org.springultron.oss.http;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springultron.oss.service.OssService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nox
 * @Description aws 对外提供服务端点
 * @date 2020/8/20
 */
@RestController
@RequestMapping("/oss")
public class OssEndpoint {


	private final OssService service;

	public OssEndpoint(OssService service) {
		this.service = service;
	}

	/**
	 * Bucket Endpoints
	 */
	
	@PostMapping("/bucket/{bucketName}")
	public Bucket createBucker(@PathVariable String bucketName) {

		service.createBucket(bucketName);
		return service.getBucket(bucketName).get();

	}

	
	@GetMapping("/bucket")
	public List<Bucket> getBuckets() {
		return service.getAllBuckets();
	}

	
	@GetMapping("/bucket/{bucketName}")
	public Bucket getBucket(@PathVariable String bucketName) {
		return service.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
	}

	
	@DeleteMapping("/bucket/{bucketName}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteBucket(@PathVariable String bucketName) {
		service.removeBucket(bucketName);
	}

	/**
	 * Object Endpoints
	 */
	
	@PostMapping("/object/{bucketName}")
	public S3Object createObject(@RequestBody MultipartFile object, @PathVariable String bucketName) throws Exception {
		String name = object.getOriginalFilename();
		service.putObject(bucketName, name, object.getInputStream(), object.getSize(), object.getContentType());
		return service.getObjectInfo(bucketName, name);

	}

	
	@PostMapping("/object/{bucketName}/{objectName}")
	public S3Object createObject(@RequestBody MultipartFile object, @PathVariable String bucketName,
								 @PathVariable String objectName) throws Exception {
		service.putObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
		return service.getObjectInfo(bucketName, objectName);

	}

	
	@GetMapping("/object/{bucketName}/{objectName}")
	public List<S3ObjectSummary> filterObject(@PathVariable String bucketName, @PathVariable String objectName) {

		return service.getAllObjectsByPrefix(bucketName, objectName, true);

	}

	
	@GetMapping("/object/{bucketName}/{objectName}/{expires}")
	public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName,
										 @PathVariable Integer expires) {
		Map<String, Object> responseBody = new HashMap<>(8);
		// Put Object info
		responseBody.put("bucket", bucketName);
		responseBody.put("object", objectName);
		responseBody.put("url", service.getObjectURL(bucketName, objectName, expires));
		responseBody.put("expires", expires);
		return responseBody;
	}

	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/object/{bucketName}/{objectName}/")
	public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) throws Exception {

		service.removeObject(bucketName, objectName);
	}
}
