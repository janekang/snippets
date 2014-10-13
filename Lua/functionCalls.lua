--[[
Explores function call returns and discarded results

Jane Kang, Agent Developer Intern
--]]

function exm()
	return "a", "b"
	end
function empty()
	end

x, y = exm()
print(x == "a")
print(y == "b")

x, y = exm(), 10
print(x == "a")
print(y ~= "b")

x, y = (exm())
print(x == "a")
print(y ~= "b")

x, y, z = exm()
print(x == "a" and y == "b")
print(z == nil)

x, y, z = empty(), exm()
print(x == nil and y == "a" and z == "b")

print(exm() .. "c") -- ac
print(empty() == nil)

print(exm() .. "c") -- ac

x = {empty()}
print(x[1] == nil)
x = {exm()}
print(x[1] == "a" and x[2] == "b")
x = {exm(), "c"}
print(x[1] == "a" and x[2] == "c")
