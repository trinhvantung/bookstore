import { Button, Col, Form, Input, notification, Row, Spin } from 'antd';
import React, { useState } from 'react';
import { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import userApi from '../api/userApi';
import Helmet from '../components/Helmet';

const emailValidationRegex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(.\w{2,3})+$/

const Login = () => {
    const [formLogin] = Form.useForm();
    const [formRegister] = Form.useForm();
    const [formForgotPassword] = Form.useForm();
    const [loginStatus, setLoginStatus] = useState(1);
    const [isLoading, setIsLoading] = useState(false)
    const history = useHistory()

    useEffect(() => {
        if (loginStatus === 1) {
            document.title = "Đăng nhập"
        } else if (loginStatus === 2) {
            document.title = "Đăng ký"
        } else if (loginStatus === 3) {
            document.title = "Quên mật khẩu"
        }
    }, [loginStatus])

    const login = (data) => {
        setIsLoading(true)
        userApi.login(data).then(res => {
            console.log(res);
            localStorage.setItem("accessToken", res.token)
            localStorage.setItem("type", "Bearer")
            setIsLoading(false)
            history.push("/")
        })
    }

    const register = (data) => {
        setIsLoading(true)
        userApi.register(data).then(res => {
            console.log(res);
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });
            setLoginStatus(1)
            formLogin.setFields([
                {
                    name: 'email',
                    value: data.email
                }
            ])

            formRegister.resetFields()
            setIsLoading(false)
        }).catch(err => {
            console.log(err);
            const res = err.response.data
            if (res.code === 8000) {
                formRegister.setFields([
                    {
                        name: 'email',
                        errors: [res.message]
                    }
                ])
            }
        })
    }

    const forgot = (data) => {
        console.log(data);
    }


    return (
        <Helmet title='Đăng nhập'>
            <div className='container'>
                <div className="login">
                    <div className="login__content">
                        <div className={loginStatus === 3 ? 'd-none' : ''}>
                            <div className="login__tab__list">
                                <div className={`login__tab__button${loginStatus === 1 ? ' active' : ''}`}
                                    onClick={() => setLoginStatus(1)}>Đăng nhập</div>
                                <div className={`login__tab__button${loginStatus === 2 ? ' active' : ''}`}
                                    onClick={() => setLoginStatus(2)}>Đăng ký</div>
                            </div>
                            <div className="login__body">
                                <div className={loginStatus === 2 ? 'd-none' : ''}>
                                    <Spin spinning={isLoading}>
                                        <Form
                                            form={formLogin}
                                            layout="vertical"
                                            validateTrigger="onSubmit"
                                            requiredMark={false}
                                            onFinish={login}
                                        >
                                            <Form.Item label="Email" name='email'
                                                rules={[
                                                    {
                                                        required: true,
                                                        message: "Email không được để trống"
                                                    }
                                                ]}
                                            >
                                                <Input placeholder="Email" size="large" />
                                            </Form.Item>
                                            <Form.Item
                                                label="Mật khẩu" name='password'
                                                rules={[
                                                    {
                                                        required: true,
                                                        message: "Mật khẩu không được để trống"
                                                    }
                                                ]}
                                            >
                                                <Input.Password placeholder="Mật khẩu"
                                                    type="password" size="large" />
                                            </Form.Item>
                                            <Form.Item>
                                                <div className="login__button">
                                                    <Button type="primary" htmlType="submit"
                                                        size="large">Đăng nhập</Button>
                                                </div>
                                            </Form.Item>
                                            <Form.Item>
                                                <div className="forgot-password__button"
                                                    onClick={() => setLoginStatus(3)}>Quên mật khẩu</div>
                                            </Form.Item>
                                        </Form>
                                    </Spin>
                                </div>

                                <div className={loginStatus === 1 ? 'd-none' : ''}>
                                    <Form
                                        form={formRegister}
                                        layout="vertical"
                                        validateTrigger="onSubmit"
                                        requiredMark={false}
                                        onFinish={register}
                                    >
                                        <Form.Item label="Email" name='email'
                                            rules={[
                                                {
                                                    required: true,
                                                    message: "Email không được để trống"
                                                },
                                                {
                                                    validator: async (_, value) => {
                                                        if (value && !value.match(emailValidationRegex)) {
                                                            return Promise.reject('Email không đúng định dạng');
                                                        }
                                                        return Promise.resolve()
                                                    }
                                                }
                                            ]}>
                                            <Input placeholder="Email" size="large" />
                                        </Form.Item>
                                        <Form.Item label="Họ tên" name='fullname'
                                            rules={[
                                                {
                                                    required: true,
                                                    message: "Họ tên không được để trống"
                                                },
                                                {
                                                    validator: async (_, value) => {
                                                        if (value && (value.length < 5 || value.length > 30)) {
                                                            return Promise.reject('Họ tên có độ dài từ 5 đến 30 ký tự');
                                                        }
                                                        return Promise.resolve()
                                                    }
                                                }
                                            ]}>
                                            <Input placeholder="Họ tên" size="large" />
                                        </Form.Item>
                                        <Form.Item
                                            label="Mật khẩu" name='password'
                                            rules={[
                                                {
                                                    required: true,
                                                    message: "Mật khẩu không được để trống"
                                                },
                                                {
                                                    validator: async (_, value) => {
                                                        if (value && (value.length < 5 || value.length > 30)) {
                                                            return Promise.reject('Mật khẩu có độ dài từ 5 đến 30 ký tự');
                                                        }
                                                        return Promise.resolve()
                                                    }
                                                }
                                            ]}
                                        >
                                            <Input.Password placeholder="Mật khẩu"
                                                type="password" size="large" />
                                        </Form.Item>
                                        <Form.Item
                                            label="Nhập lại mật khẩu" name='confirmPassword'
                                            rules={[
                                                {
                                                    required: true,
                                                    message: "Mật khẩu không được để trống"
                                                },
                                                {
                                                    validator: async (_, value) => {
                                                        if (value && value !== formRegister.getFieldValue('password')) {
                                                            return Promise.reject('Mật khẩu không trùng khớp');
                                                        }
                                                        return Promise.resolve()
                                                    }
                                                }
                                            ]}
                                        >
                                            <Input.Password placeholder="Nhập lại mật khẩu"
                                                type="password" size="large" />
                                        </Form.Item>
                                        <Form.Item>
                                            <div className="login__button">
                                                <Button type="primary" htmlType='submit'
                                                    size="large">Đăng ký</Button>
                                            </div>
                                        </Form.Item>
                                    </Form>
                                </div>
                            </div>
                        </div>
                        <div className={loginStatus !== 3 ? 'd-none' : ''}>
                            <div className="forgot-password__title">KHÔI PHỤC MẬT KHẨU</div>
                            <div className="login__body">
                                <Form
                                    form={formForgotPassword}
                                    layout="vertical"
                                    validateTrigger="onSubmit"
                                    requiredMark={false}
                                    onFinish={forgot}
                                >
                                    <Form.Item label="Email">
                                        <Row gutter={8}>
                                            <Col span={18}>
                                                <Form.Item
                                                    name="captcha"
                                                    noStyle
                                                    rules={[
                                                        {
                                                            required: true,
                                                            message: "Email không được để trống"
                                                        },
                                                        {
                                                            validator: async (_, value) => {
                                                                if (value && !value.match(emailValidationRegex)) {
                                                                    return Promise.reject('Email không đúng định dạng');
                                                                }
                                                                return Promise.resolve()
                                                            }
                                                        }
                                                    ]}
                                                >
                                                    <Input placeholder="Email" size="large" />
                                                </Form.Item>
                                            </Col>
                                            <Col span={6}>
                                                <Button style={{ width: '100%' }} htmlType='submit'
                                                    size="large">Gửi</Button>
                                            </Col>
                                        </Row>
                                    </Form.Item>
                                    <Form.Item>
                                        <div className="login__button">
                                            <Button size="large"
                                                onClick={() => setLoginStatus(1)}>Quay lại</Button>
                                        </div>
                                    </Form.Item>
                                </Form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </Helmet>
    )
}

export default Login