import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author mlq
 * @date 2018/8/9
 */
@RunWith(MockitoJUnitRunner.class)
public class TestV1 {
  private static final Logger logger = LoggerFactory.getLogger(TestV1.class);
  @Mock
  List mockList;

  @Test
  public void verify_behavior() {
    List mock = mock(List.class);
    mock.add(1);
    mock.clear();
    verify(mock).add(1);
    verify(mock).clear();
  }

  @Test
  public void when_thenReturn() {
    //mock一个Iterator类
    Iterator iterator = mock(Iterator.class);
    //预设当iterator调用next()时第一次返回hello，第n次都返回world
    when(iterator.next()).thenReturn("hello").thenReturn("world");
    //使用mock的对象
    String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
    //验证结果
    assertEquals("hello world world", result);
  }

  @Test(expected = IOException.class)
  public void when_thenThrow() throws IOException {
    OutputStream outputStream = mock(OutputStream.class);
    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
    //预设当流关闭时抛出异常
    doThrow(new IOException()).when(outputStream).close();
    outputStream.close();
  }

  @Test
  public void returnsSmartNullsTest() {
    List mock = mock(List.class, RETURNS_SMART_NULLS);
    System.out.println(mock.get(0));

    //使用RETURNS_SMART_NULLS参数创建的mock对象，不会抛出NullPointerException异常。另外控制台窗口会提示信息“SmartNull returned by unstubbed get() method on mock”
    System.out.println(mock.toArray().length);
  }

  @Test
  public void deepstubsTest() {
    Account account = mock(Account.class, RETURNS_DEEP_STUBS);
    //when(account.getRailwayTicket().getDestination()).thenReturn("Beijing");
    doReturn("ww").when(account).getRailwayTicket().getDestination();
    account.getRailwayTicket().getDestination();
    verify(account.getRailwayTicket()).getDestination();
    assertEquals("Beijing", account.getRailwayTicket().getDestination());
  }

  @Test
  public void deepstubsTest2() {
    Account account = mock(Account.class);
    RailwayTicket railwayTicket = mock(RailwayTicket.class);
    when(account.getRailwayTicket()).thenReturn(railwayTicket);
    when(railwayTicket.getDestination()).thenReturn("Beijing");

    account.getRailwayTicket().getDestination();
    verify(account.getRailwayTicket()).getDestination();
    assertEquals("Beijing", account.getRailwayTicket().getDestination());
  }

  public class RailwayTicket {
    private String destination;

    public String getDestination() {
      return destination;
    }

    public void setDestination(String destination) {
      this.destination = destination;
    }
  }

  public class Account {
    private RailwayTicket railwayTicket;

    public RailwayTicket getRailwayTicket() {
      return railwayTicket;
    }

    public void setRailwayTicket(RailwayTicket railwayTicket) {
      this.railwayTicket = railwayTicket;
    }
  }

  @Test(expected = RuntimeException.class)
  public void doThrow_when() {
    List list = mock(List.class);
    doThrow(new RuntimeException()).when(list).add(1);
    list.add(1);
  }

  @Test
  public void with_arguments() {
    Comparable comparable = mock(Comparable.class);
    //预设根据不同的参数返回不同的结果
    when(comparable.compareTo("Test")).thenReturn(1);
    when(comparable.compareTo("Omg")).thenReturn(2);
    assertEquals(1, comparable.compareTo("Test"));
    assertEquals(2, comparable.compareTo("Omg"));
    //对于没有预设的情况会返回默认值
    assertEquals(0, comparable.compareTo("Not stub"));
  }

  @Test
  public void argumentMatchersTest() {
    //创建mock对象
    List<String> mock = mock(List.class);

    //argThat(Matches<T> matcher)方法用来应用自定义的规则，可以传入任何实现Matcher接口的实现类。
    when(mock.addAll(argThat(new IsListofTwoElements()))).thenReturn(true);

    mock.addAll(Arrays.asList("one", "two", "three"));
    //IsListofTwoElements用来匹配size为2的List，因为例子传入List为三个元素，所以此时将失败。
    verify(mock).addAll(argThat(new IsListofTwoElements()));
  }

  @Test
  public void all_arguments_provided_by_matchers() {
    Comparator comparator = mock(Comparator.class);
    comparator.compare("nihao", "hello");
    //如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
    verify(comparator).compare(anyString(), eq("hello"));
    //下面的为无效的参数匹配使用
    //verify(comparator).compare(anyString(),"hello");
  }

  class IsListofTwoElements extends ArgumentMatcher<List> {
    public boolean matches(Object list) {
      return ((List) list).size() == 2;
    }
  }

  @Test
  public void capturing_args() {
    PersonDao personDao = mock(PersonDao.class);
    PersonService personService = new PersonService(personDao);

    ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
    personService.update(1, "jack");
    verify(personDao).update(argument.capture());
    assertEquals(1, argument.getValue().getId());
    assertEquals("jack", argument.getValue().getName());
  }

  class Person {
    private int id;
    private String name;

    Person(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }
  }

  interface PersonDao {
    public void update(Person person);
  }

  class PersonService {
    private PersonDao personDao;

    PersonService(PersonDao personDao) {
      this.personDao = personDao;
    }

    public void update(int id, String name) {
      personDao.update(new Person(id, name));
    }
  }

  @Test
  public void answerTest() {
    when(mockList.get(anyInt())).thenAnswer(new CustomAnswer());
    assertEquals("hello world:0", mockList.get(0));
    assertEquals("hello world:999", mockList.get(999));
  }

  private class CustomAnswer implements Answer<String> {
    @Override
    public String answer(InvocationOnMock invocation) throws Throwable {
      Object[] args = invocation.getArguments();
      return "hello world:" + args[0];
    }
  }
  //https://www.cnblogs.com/Ming8006/p/6297333.html

  @Test
  public void unstubbed_invocations() {
    //mock对象使用Answer来对未预设的调用返回默认期望值
    List mock = mock(List.class, new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        return 999;
      }
    });
    //下面的get(1)没有预设，通常情况下会返回NULL，但是使用了Answer改变了默认期望值
    assertEquals(999, mock.get(1));
    //下面的size()没有预设，通常情况下会返回0，但是使用了Answer改变了默认期望值
    assertEquals(999, mock.size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void spy_on_real_objects() {
    List list = new LinkedList();
    List spy = spy(list);
    //下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
    //when(spy.get(0)).thenReturn(3);

    //使用doReturn-when可以避免when-thenReturn调用真实对象api
    doReturn(999).when(spy).get(999);
    //预设size()期望值
    when(spy.size()).thenReturn(100);
    //调用真实对象的api
    spy.add(1);
    spy.add(2);
    assertEquals(100, spy.size());
    assertEquals(1, spy.get(0));
    assertEquals(2, spy.get(1));
    verify(spy).add(1);
    verify(spy).add(2);
    assertEquals(999, spy.get(999));
    spy.get(2);
  }

  @Test
  public void real_partial_mock() {
    //通过spy来调用真实的api
    List list = spy(new ArrayList());
    assertEquals(0, list.size());
    A a = mock(A.class);
    //通过thenCallRealMethod来调用真实的api
    when(a.doSomething(anyInt())).thenCallRealMethod();
    assertEquals(999, a.doSomething(999));
  }


  class A {
    public int doSomething(int i) {
      return i;
    }
  }

  @Test
  public void reset_mock() {
    List list = mock(List.class);
    when(list.size()).thenReturn(10);
    list.add(1);
    assertEquals(10, list.size());
    //重置mock，清除所有的互动和预设
    reset(list);
    assertEquals(0, list.size());
  }


  @Test
  public void verifying_number_of_invocations() {
    List list = mock(List.class);
    list.add(1);
    list.add(2);
    list.add(2);
    list.add(3);
    list.add(3);
    list.add(3);
    //验证是否被调用一次，等效于下面的times(1)
    verify(list).add(1);
    verify(list, times(1)).add(1);
    //验证是否被调用2次
    verify(list, times(2)).add(2);
    //验证是否被调用3次
    verify(list, times(3)).add(3);
    //验证是否从未被调用过
    verify(list, never()).add(4);
    //验证至少调用一次
    verify(list, atLeastOnce()).add(1);
    //验证至少调用2次
    verify(list, atLeast(2)).add(2);
    //验证至多调用3次
    verify(list, atMost(3)).add(3);
  }

  @Test(expected = RuntimeException.class)
  public void consecutive_calls() {
    //模拟连续调用返回期望值，如果分开，则只有最后一个有效
    when(mockList.get(0)).thenReturn(0);
    when(mockList.get(0)).thenReturn(1);
    when(mockList.get(0)).thenReturn(2);
    when(mockList.get(1)).thenReturn(0).thenReturn(1).thenThrow(new RuntimeException());
    assertEquals(2, mockList.get(0));
    assertEquals(2, mockList.get(0));
    assertEquals(0, mockList.get(1));
    assertEquals(1, mockList.get(1));
    //第三次或更多调用都会抛出异常
    mockList.get(1);
  }

  @Test
  public void verification_in_order() {
    List list = mock(List.class);
    List list2 = mock(List.class);
    list.add(1);
    list2.add("hello");
    list.add(2);
    list2.add("world");
    //将需要排序的mock对象放入InOrder
    InOrder inOrder = inOrder(list, list2);
    //下面的代码不能颠倒顺序，验证执行顺序
    inOrder.verify(list).add(1);
    inOrder.verify(list2).add("hello");
    inOrder.verify(list).add(2);
    inOrder.verify(list2).add("world");
  }

  @Test
  public void verify_interaction() {
    List list = mock(List.class);
    List list2 = mock(List.class);
    List list3 = mock(List.class);
    list.add(1);
    verify(list).add(1);
    verify(list, never()).add(2);
    //验证零互动行为
    verifyZeroInteractions(list2, list3);
  }

  @Test(expected = NoInteractionsWanted.class)
  public void find_redundant_interaction() {
    List list = mock(List.class);
    list.add(1);
    list.add(2);
    verify(list, times(2)).add(anyInt());
    //检查是否有未被验证的互动行为，因为add(1)和add(2)都会被上面的anyInt()验证到，所以下面的代码会通过
    verifyNoMoreInteractions(list);

    List list2 = mock(List.class);
    list2.add(1);
    list2.add(2);
    verify(list2).add(1);
    //检查是否有未被验证的互动行为，因为add(2)没有被验证，所以下面的代码会失败抛出异常
    verifyNoMoreInteractions(list2);
  }

  @Test
  public void testReturn() {
    //mock出来的对象不论是when thenReturn还是doReturn when都不会真正的调用函数
    //spy出来的对象when thenReturn会真的调用函数,doReturn when不会
    // Animal a = spy(new Animal());
    //when(a.eat()).thenReturn("he");
    // doReturn("ere").when(a).eat();
    //System.out.println(a.eat());
    // Animal a = mock(Animal.class);

//    Animal a = spy(new Animal());
//    System.out.println(a);
//    People people = spy(new People());
//    when(people.test(any())).thenReturn("12345");
//    System.out.println(people.test(a));
  }

  @Test
  public void testMock() {
    // Animal a = spy(new Animal());
    Animal a = mock(Animal.class);
    //when(a.speak()).thenReturn("jjjjj"); //执行方法拦截return,会走真实方法
    doReturn("bbbb").when(a).speak();//匹配方法返回return,不会走真实方法
    System.out.println(a.speak());
//    People p = spy(new People());
//    Animal a = mock(Animal.class);//animal把
//    when(a.speak()).thenReturn("happy");
//   // when(p.eat(any())).thenReturn("sad");
//    System.out.println(p.eat(a));
  }


}
