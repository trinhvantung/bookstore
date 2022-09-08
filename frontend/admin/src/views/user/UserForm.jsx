import { Button, Form, Input, notification, Radio, Spin } from 'antd';
import queryString from 'query-string';
import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import userApi from '../../api/userApi';

const UserForm = (props) => {
    const location = useLocation();

    const [isLoading, setIsLoading] = useState(false)
    const [disableStatus, setDisableStatus] = useState(false);
    const [form] = Form.useForm()

    const handleSubmit = (data) => {
        setIsLoading(true)
        console.log(data)

        const id = queryString.parse(location.search).id
        userApi.update(id, data).then(res => {
            console.log(res);
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });
        }).catch(err => {
            console.log(err);
            const data = err.response.data
            if (data.code === 6000) {
                form.setFields([
                    {
                        name: "name",
                        errors: [data.message]
                    }
                ])
            }
        }).finally(() => {
            setIsLoading(false)
        })
    }

    useEffect(() => {
        form.resetFields()

        const id = queryString.parse(location.search).id
        userApi.get(id).then(res => {
            console.log(res);
            form.setFields([
                {
                    name: "fullname",
                    value: res.fullname
                },
                {
                    name: "id",
                    value: res.id
                },
                {
                    name: "email",
                    value: res.email
                },
                {
                    name: "status",
                    value: res.status
                }
            ])

            if(res.roles.some(role => role.name === 'ADMIN')) {
                setDisableStatus(true)
            }
        }).catch(err => {
        })

    }, [location.pathname])


    return (
        <Spin spinning={isLoading}>
            <Form
                name="form"
                form={form}
                validateTrigger="onSubmit"
                labelCol={{ flex: '110px' }}
                labelAlign="left"
                labelWrap
                wrapperCol={{ flex: 1 }}
                autoComplete="off"
                onFinish={handleSubmit}
                requiredMark={false}
            >
                <Form.Item
                    label="ID"
                    name="id"
                >
                    <Input placeholder="ID" disabled />
                </Form.Item>
                <Form.Item
                    label="Fullname"
                    name="fullname"
                    rules={[
                        {
                            required: true,
                            message: "Fullname cannot be empty"
                        }
                    ]}
                >
                    <Input placeholder="Fullname" />
                </Form.Item>
                <Form.Item
                    label="Email"
                    name="email"
                    rules={[
                        {
                            required: true,
                            message: "Email cannot be empty"
                        },
                        {
                            validator: async (_, value) => {
                                if (value && !value.match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)) {
                                    return Promise.reject('Email invalidate');
                                }
                                return Promise.resolve()
                            }
                        }
                    ]}
                >
                    <Input placeholder="Email" />
                </Form.Item>

                <Form.Item
                    label="Mật khẩu"
                    name="password"
                    rules={[
                        {
                            validator: async (_, value) => {
                                if (value && value.length < 5) {
                                    return Promise.reject('Password at least 5 characters');
                                }
                                return Promise.resolve()
                            }
                        }
                    ]}
                >
                    <Input placeholder="Mật khẩu" type="password" />
                </Form.Item>
                <Form.Item
                    label="Status"
                    name="status"
                    initialValue={false}
                >
                    <Radio.Group disabled={disableStatus}>
                        <Radio value={true}>Active</Radio>
                        <Radio value={false}>Inactive</Radio>
                    </Radio.Group>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" disabled={isLoading}>
                        Save
                    </Button>
                </Form.Item>
            </Form>
        </Spin>
    )
}

export default UserForm