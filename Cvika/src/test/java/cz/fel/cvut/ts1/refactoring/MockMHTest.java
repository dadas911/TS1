package cz.fel.cvut.ts1.refactoring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class MockMHTest {

    DBManager mockDBManager = Mockito.mock(DBManager.class);
    MailHelper mailHelper = new MailHelper(mockDBManager);

    @Test
    public void negativeVerifyTwoTimes()
    {
        mailHelper.sendMail(Mockito.anyInt());
        Mockito.verify(mockDBManager, times(2)).findMail(Mockito.anyInt());
    }

    @Test
    public void positiveVerifySendMailOnce()
    {
        mailHelper.sendMail(Mockito.anyInt());
        Mockito.verify(mockDBManager).findMail(Mockito.anyInt());
    }

    @Test
    public void mockTest()
    {
        int mailId = 1;
        Mail mail = getMail();
        Mockito.when(mockDBManager.findMail(mailId)).thenReturn(mail);
        mailHelper.sendMail(mailId);
        Mockito.verify(mockDBManager).findMail(mailId);
    }

    @Test
    public void checkMailReturnsCorrectTo()
    {
        int mailId = Mockito.anyInt();
        Mockito.when(mockDBManager.findMail(mailId)).thenReturn(getMail());
        mailHelper.sendMail(mailId);
        Assertions.assertEquals("ABCD", mailHelper.getMail().getTo());
    }

    @Test
    public void checkMailReturnsCorrectBody()
    {
        int mailId = Mockito.anyInt();
        Mockito.when(mockDBManager.findMail(mailId)).thenReturn(getMail());
        mailHelper.sendMail(mailId);
        Assertions.assertEquals("BODY", mailHelper.getMail().getBody());
    }

    @Test
    public void checkMailReturnsCorrectSubject()
    {
        int mailId = Mockito.anyInt();
        Mockito.when(mockDBManager.findMail(mailId)).thenReturn(getMail());
        mailHelper.sendMail(mailId);
        Assertions.assertEquals("SUBJECT", mailHelper.getMail().getSubject());
    }

    private Mail getMail()
    {
        Mail mail = new Mail();
        mail.setTo("ABCD");
        mail.setBody("BODY");
        mail.setSubject("SUBJECT");

        return mail;
    }
}
