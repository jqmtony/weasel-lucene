package com.weasel.lucene.test;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.weasel.lucene.test.domain.User;
import com.weasel.lucene.test.repository.UserRepository;

/**
 * 
 * @author Dylan
 * @time 2013-7-8
 */
public class UserRepositoryTest {

	private UserRepository repository = null;
	@Before
	public void inject(){
		repository = new UserRepository();
	}
	@Test
	public void testSave(){
		User user = new User();
		user.set_id("123456");
		user.setUsername("李四");
		user.setPassword("abc123");
		user.setContent("李四是个好学生!");
		repository.save(user);
	}
	@Test
	public void testDeleteAll(){
		repository.deleteAll();
	}
	@Test
	public void testDelete(){
		repository.delete("123456");
		User user = repository.findOneById("123456");
		Assert.assertTrue(null == user);
	}
	@Test
	public void testFindOneById(){
		User user = repository.findOneById("123456");
		Assert.assertTrue(null != user);
	}
	@Test
	public void testUpdate(){
		User user = repository.findOneById("123456");
		Assert.assertTrue(StringUtils.equals("李四是个好学生!", user.getContent()));
		user.setContent("张三是个好学生!");
		repository.update(user);
		Assert.assertTrue(StringUtils.equals("张三是个好学生!", user.getContent()));
	}
	
	
}
