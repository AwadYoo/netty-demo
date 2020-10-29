
## 1.根据项目唯一id获取对应项目的项目概况

#### 接口URL
> http://localhost:8999/getProjectById

#### 请求方式
> POST

#### Content-Type
> json

#### 请求Query参数

| 参数        | 示例值   | 是否必填   |  参数描述  |
| :--------   | :-----  | :-----  | :----  |
| projectId     | 12354 | 必填 | - |





#### 请求Body参数

```javascript
{
	"projectId": "projectId"
}
```

#### 成功响应示例
```javascript
{
	"code": 0,
	"msg": "success",
	"data": {
		"projectName": "VII-01标-工程土建项目",
		"area": 2000,
		"price": 3000,
		"progress": 0.18
	}
}
```

| 参数        | 示例值   |  参数描述  |
| :--------   | :-----  | :----  |
| code     | 0 | 响应结果 |
| msg     | success | 响应信息 |
| data     | - | 数据 |
| data.projectName     | VII-01标-工程土建项目 | - |
| data.area     | 2000 | 建筑面积 |
| data.price     | 3000 | 造价 |
| data.progress     | 0.18 | 项目进度 |

#### 错误响应示例
```javascript
{
	"code": 1,
	"msg": "fail",
	"data": null
}
```

| 参数        | 示例值   |  参数描述  |
| :--------   | :-----  | :----  |
| code     | 1 | 响应结果 |
| msg     | fail | 响应信息 |
| data     | - | 数据 |

## 1.获取主要岗位人员在岗情况近五个月月统计

#### 接口URL
> http://localhost:8999/getJobOnlineMonth

#### 请求方式
> POST

#### Content-Type
> json






#### 请求Body参数

```javascript
{
	"projectId": "projectId"
}
```

#### 成功响应示例
```javascript
{
	"code": 0,
	"msg": "success",
	"data": {
		"range": "范围内数据",
		"unit": "人",
		"detail": [
			{
				"month": 10,
				"num": 486
			},
			{
				"month": 9,
				"num": 205
			},
			{
				"month": 8,
				"num": 411
			},
			{
				"month": 7,
				"num": 471
			},
			{
				"month": 6,
				"num": 50
			}
		]
	}
}
```

| 参数        | 示例值   |  参数描述  |
| :--------   | :-----  | :----  |
| code     | 0 | 响应结果 |
| msg     | success | 响应信息 |
| data     | - | 数据 |
| data.range     | 范围内数据 | - |
| data.unit     | 人 | 数据单位 |
| data.detail     | - | - |
| data.detail.month     | 10 | 月份值 |
| data.detail.num     | 486 | 人数 |

#### 错误响应示例
```javascript
{
	"code": 1,
	"msg": "fail",
	"data": null
}
```

| 参数        | 示例值   |  参数描述  |
| :--------   | :-----  | :----  |
| code     | 1 | 响应结果 |
| msg     | fail | 响应信息 |
| data     | - | 数据 |

## 2.获取一卡通总数和当日刷卡数（日、时、分，越精确越好）

#### 接口URL
> http://localhost:8999/getCardCount

#### 请求方式
> POST

#### Content-Type
> json






#### 请求Body参数

```javascript
{
	"projectId": "projectId"
}
```

#### 成功响应示例
```javascript
{
	"code": 0,
	"msg": "success",
	"data": {
		"totalNum": 428,
		"usedCount": 127
	}
}
```

| 参数        | 示例值   |  参数描述  |
| :--------   | :-----  | :----  |
| code     | 0 | 响应结果 |
| msg     | success | 响应信息 |
| data     | - | 数据 |
| data.totalNum     | 428 | 一卡通总数 |
| data.usedCount     | 127 | 此刻已刷卡数 |

#### 错误响应示例
```javascript
{
	"code": 1,
	"msg": "fail",
	"data": null
}
```

| 参数        | 示例值   |  参数描述  |
| :--------   | :-----  | :----  |
| code     | 1 | 响应结果 |
| msg     | fail | 响应信息 |
| data     | - | 数据 |

## readme
**暂时保留**
