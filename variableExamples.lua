--[[
Explores variable scopes

Jane Kang, Agent Developer Intern
--]]

x = 10
local i = 1

-- Print even num up to 20 (2*10)
while i <= x do
	local x = i*2
	print(x)
	i = i + 1
end

if i > 20 then
	local x
	x = 20
	print(x+2)
else
	print(x)
end

print(x)


-- Local scope begins after declaration, goes until end of block
local a, b = 1, 10
if a < b then
	print(a) -- 1
	local a
	print(a) -- nil, no value
end
print(a, b)

-- Explicit block
do
	local c = 2*a
	local d = b^2 - 4*a*c
	x1 = (-b + d)/c
	x2 = (-b - d)/c
end -- c, d scope ends
print(x1, x2)

