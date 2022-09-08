import { Button, Form, Input, notification, Spin } from 'antd';
import queryString from 'query-string';
import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import publisherApi from '../../api/publisherApi';

const PublisherForm = (props) => {
    const location = useLocation();

    const [isLoading, setIsLoading] = useState(false)
    const [form] = Form.useForm()

    const handleSubmit = (data) => {
        setIsLoading(true)
        if (location.pathname === '/publisher/update') {
            const id = queryString.parse(location.search).id
            publisherApi.update(id, data).then(res => {
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
        } else {
            publisherApi.add(data).then(res => {
                console.log(res);
                notification.info({
                    message: `Notification`,
                    description: 'Success',
                    placement: 'top',
                });
                form.resetFields()

            }).catch(err => {
                const data = err.response.data
                if (data.code === 6000) {
                    form.setFields([
                        {
                            name: "name",
                            errors: [data.message]
                        }
                    ])
                }
                console.log(data)
            }).finally(() => {
                setIsLoading(false)
            })
        }
    }

    useEffect(() => {
        form.resetFields()
        

        if (location.pathname === '/publisher/update') {
            const id = queryString.parse(location.search).id
            publisherApi.get(id).then(res => {
                console.log(res);
                form.setFields([
                    {
                        name: "name",
                        value: res.name
                    },
                    {
                        name: "id",
                        value: res.id
                    }
                ])
            }).catch(err => {
            })
        }

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
                    label="Name"
                    name="name"
                    rules={[
                        {
                            required: true,
                            message: "Publisher name cannot be empty"
                        }
                    ]}
                >
                    <Input placeholder="Name" />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" disabled={isLoading}>
                        Lưu
                    </Button>
                </Form.Item>
            </Form>
        </Spin>
    )
}

export default PublisherForm